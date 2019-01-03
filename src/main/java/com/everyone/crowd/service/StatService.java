package com.everyone.crowd.service;

import java.util.Date;

public interface StatService {

    int getDemandsCount(Date publishTime);

    int getDemandsCount();

    int getDemandsCount(String status);

    int getOrdersCount(Date orderTime);

    int getDevelopersCount(String status);

    int getCustomersCount(String status);

    int getUsersCount();

    int getFeedbacksCount();
}
