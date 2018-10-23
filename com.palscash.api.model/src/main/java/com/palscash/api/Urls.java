package com.palscash.api;

public class Urls {

	public static final String URL_PING = "/api/ping";

	public static final String URL_GET_TX_FEE = "/api/fee";

	public static final String URL_GET_NEW_ACCOUNT = "/api/account/new";

	public static final String URL_GET_RESTORE_ACCOUNT = "/api/account/restore";

	public static final String URL_GET_BALANCE = "/api/account/balance/{address}";

	public static final String URL_GET_ACCOUNT = "/api/account/get/{address}";

	public static final String URL_TRANSFER = "/api/transfer";

	public static final String URL_GET_TX_DETAILS = "/api/tx/{uuid}";

	public static final String URL_GET_TX_COUNT = "/api/tx/count/{uuid}";

	public static final String URL_LIST_TX_FROM = "/api/list/tx/from/{uuid}";

	public static final String URL_LIST_TX_TO = "/api/list/tx/to/{uuid}";

}
