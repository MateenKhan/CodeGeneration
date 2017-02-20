package com.code.genreation.common;











public class SqlQuerys{

	public final class Company{

		public static final String INSERT_QRY = "INSERT INTO company VALUES( `id`, `name`, `ein`, `type`, `phone_number`, `address`, `city`, `state`, `country`, `zipcode`, `currency`, `email`, `payment_info`, `createdby`, `modifiedby`, `createddate`, `modifieddate`, `owner`, `active` ) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );";

		public static final String UPDATE_QRY = "UPDATE company SET `name` = ?, `ein` = ?, `type` = ?, `phone_number` = ?, `address` = ?, `city` = ?, `state` = ?, `country` = ?, `zipcode` = ?, `currency` = ?, `email` = ?, `payment_info` = ?, `createdby` = ?, `modifiedby` = ?, `createddate` = ?, `modifieddate` = ?, `owner` = ?, `active` = ? WHERE `id` = ?;";

		public static final String DELETE_QRY = "DELETE FROM company WHERE  WHERE `id` = ?;";

		public static final String GET_QRY = "SELECT `id`, `name`, `ein`, `type`, `phone_number`, `address`, `city`, `state`, `country`, `zipcode`, `currency`, `email`, `payment_info`, `createdby`, `modifiedby`, `createddate`, `modifieddate`, `owner`, `active` from `company` WHERE `id` = ?;";

		public static final String GET_ALL_QRY = "SELECT * FROM company;";

	}
}