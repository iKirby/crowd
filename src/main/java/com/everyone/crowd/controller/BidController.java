package com.everyone.crowd.controller;

import com.everyone.crowd.entity.*;
import com.everyone.crowd.entity.exception.ForbiddenException;
import com.everyone.crowd.entity.exception.NotAcceptableException;
import com.everyone.crowd.entity.exception.NotFoundException;
import com.everyone.crowd.entity.status.DemandStatus;
import com.everyone.crowd.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BidController {

    private final BidService bidService;
    private final DemandService demandService;
    private final CustomerProfileService customerProfileService;
    private final DevProfileService devProfileService;
    private final CategoryService categoryService;

    @Autowired
    public BidController(BidService bidService,
                         DemandService demandService,
                         CustomerProfileService customerProfileService,
                         DevProfileService devProfileService,
                         CategoryService categoryService) {
        this.bidService = bidService;
        this.demandService = demandService;
        this.customerProfileService = customerProfileService;
        this.devProfileService = devProfileService;
        this.categoryService = categoryService;
    }

    @GetMapping("/bid/join/{id}")
    public String joinBid(Model model, HttpSession session, @PathVariable("id") Integer demandId) {
        User user = (User) session.getAttribute("user");
        if (!user.isDeveloper()) throw new NotAcceptableException("您不是开发者，无法参与竞标");

        Demand demand = demandService.findById(demandId);
        if (demand == null) throw new NotFoundException("找不到请求的需求信息");
        if (user.getId().equals(demand.getCustomerId())) throw new NotAcceptableException("自己不能参加自己的需求的竞标");
        if (!demand.getStatus().equals(DemandStatus.PASS.name())) throw new NotAcceptableException("此需求未开放竞标");

        model.addAttribute("demand", demand);
        model.addAttribute("bid", new Bid() {{
            setDemandId(demandId);
        }});
        model.addAttribute("customer", customerProfileService.findById(demand.getCustomerId()));
        model.addAttribute("category", categoryService.findById(demand.getCategoryId()));
        return "bid-join";
    }

    @PostMapping("/bid/join")
    public String joinBid(Bid bid, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!user.isDeveloper()) throw new NotAcceptableException("您不是开发者，无法参与竞标");
        bid.setDevId(user.getId());
        bidService.participate(bid);
        return "redirect:/bid/view/" + bid.getDemandId();
    }

    @GetMapping("/bid/view/{id}")
    public String viewBids(Model model, @PathVariable("id") Integer demandId,
                           @RequestParam(value = "page", defaultValue = "1") int page) {
        Demand demand = demandService.findById(demandId);
        model.addAttribute("demand", demand);
        Page<Bid> bids = bidService.findByDemandId(demandId, 10, page);
        model.addAttribute("bids", bids);
        model.addAttribute("customer", customerProfileService.findById(demand.getCustomerId()));
        model.addAttribute("category", categoryService.findById(demand.getCategoryId()));
        if (bids.getContent().size() > 0) {
            List<Integer> ids = new ArrayList<>();
            for (Bid bid : bids.getContent()) {
                ids.add(bid.getDevId());
            }
            model.addAttribute("devIdNameMap", devProfileService.getIdNameMap(ids));
        }
        return "bid-list";
    }

    @GetMapping("/bid/detail/{id}")
    public String viewBid(Model model, @PathVariable("id") Integer bidId, HttpSession session) {
        Bid bid = bidService.findById(bidId);
        if (bid == null) throw new NotFoundException("找不到请求的竞标信息");
        Demand demand = demandService.findById(bid.getDemandId());
        if (demand.getCustomerId().equals(((User) session.getAttribute("user")).getId())) {
            DevProfile devProfile = devProfileService.findById(bid.getDevId());
            model.addAttribute("bid", bid);
            model.addAttribute("demand", demand);
            model.addAttribute("devProfile", devProfile);
            return "bid-view";
        } else {
            throw new ForbiddenException("当前账户无法查看此竞标信息");
        }
    }
}
