package com.example.megamart.managefunds.donation;

public class Donor {
    String donor_name;
    String amount;
    String mobile;

    public Donor(String donor_name, String amount, String mobile) {
        this.donor_name = donor_name;
        this.amount = amount;
        this.mobile = mobile;
    }

    public String getDonor_name() {
        return donor_name;
    }

    public String getAmount() {
        return amount;
    }

    public String getMobile() {
        return mobile;
    }
}
