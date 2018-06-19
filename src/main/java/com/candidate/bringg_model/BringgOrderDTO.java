package com.candidate.bringg_model;

import java.util.Date;

public class BringgOrderDTO {

    String access_token;
    Date timestamp;// => date('Y-m-d H:i:s'),
	long company_id;// => <THE COMPANY ID>,
    long customer_id;// => 2,
    String title;// => "Pizza Delivery",
    String address;// => "416 Water St. New York, NY 10002",
    String scheduled_at;// => "2014-11-29T04:16:00-5",
    long team_id;// => 4,
    double lat;// => 45.5,
    double lng;// => 12.5,
    String inventory;// => '[{"id": 72407, "original_quantity":25, "quantity":0}, {"id": 72408, "original_quantity":20, "quantity":0}]',
}
