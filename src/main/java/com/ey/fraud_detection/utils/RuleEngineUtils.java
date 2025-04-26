package com.ey.fraud_detection.utils;

public class RuleEngineUtils {
    public String ruleResult(int ruleAmount)
    {
        if(ruleAmount < 10000)
        {
            return "Fraud detected with risk score 10";
        }
        else if(ruleAmount < 50000)
        {
            return "Fraud detected with risk score 50";
        }
        else if(ruleAmount < 100000)
        {
            return "Fraud detected with risk score 100";
        }
        else
        {
            return "Fraud not detected";
        }
    }

}
