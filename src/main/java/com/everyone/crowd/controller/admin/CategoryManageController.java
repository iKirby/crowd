package com.everyone.crowd.controller.admin;

import com.everyone.crowd.entity.Category;
import com.everyone.crowd.entity.Message;
import com.everyone.crowd.service.CategoryService;
import com.everyone.crowd.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class CategoryManageController {

    private final CategoryService categoryService;

    public CategoryManageController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/demand/category")
    public String categoryList(Model model) {
        model.addAttribute("categoryList", categoryService.findAll());
        return "admin/category-manage";
    }

    @GetMapping("/admin/demand/category/edit/{id}")
    public String editCategory(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("category", categoryService.findById(id));
        model.addAttribute("title", "编辑分类");
        return "admin/category-edit";
    }

    @GetMapping("/admin/demand/category/new")
    public String editCategory(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("title", "新建分类");
        return "admin/category-edit";
    }

    @PostMapping("/admin/demand/category")
    public String updateCategory(HttpServletResponse response, Category category) {
        if (category.getId() != null) {
            categoryService.update(category);
        } else {
            categoryService.insert(category);
        }
        CookieUtil.addMessage(response, "admin",
                new Message(Message.TYPE_SUCCESS, "分类信息已经保存"), "/admin");
        return "redirect:/admin/demand/category/edit/" + category.getId();
    }

    @GetMapping("/admin/demand/category/delete/{id}")
    public String deleteCategory(HttpServletResponse response, @PathVariable("id") Integer id) {
        categoryService.delete(id);
        CookieUtil.addMessage(response, "admin",
                new Message(Message.TYPE_SUCCESS, "分类已经删除"), "/admin");
        return "redirect:/admin/demand/category";
    }

}
