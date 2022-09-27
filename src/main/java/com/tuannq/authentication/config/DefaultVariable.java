package com.tuannq.authentication.config;

import com.mservice.shared.sharedmodels.Environment;
import com.mservice.shared.sharedmodels.PartnerInfo;

import java.util.List;

public class DefaultVariable {
    // 7 ngày
    public static final int MAX_AGE_COOKIE = 7 * 24 * 60 * 60;

    public static final String JWT_TOKEN = "JWT_TOKEN";

    public static final List<String> IMAGE_EXTENSION = List.of("jpg", "png", "svg", "jpeg", "gif");

    public static final Integer LIMIT = 15;


    public static final PartnerInfo DEV_INFO_MOMO = new PartnerInfo(
            "MOMOFG1R20210920",
            "YMQg4OFEdd3SdfrJ",
            "gQ5pwZivZ7R3Aly1W899x3m1Yse1TgyA"
    );

    public static final Environment ENVIRONMENT_MOMO = new Environment(
            "https://test-payment.momo.vn/gw_payment/transactionProcessor",
            DEV_INFO_MOMO,
            Environment.EnvTarget.DEV);

}
