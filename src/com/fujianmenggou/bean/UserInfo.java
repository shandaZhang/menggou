package com.fujianmenggou.bean;

public class UserInfo {

	private Integer _id;
	private String uid/*,group_id,group_name*/,user_name,nick_name,mobile,idCard/*,status,discount
	,lower_number,bank_account,todayProfit,monthProfit*/
	,id_card_heads,idCard_tails,bank_card_heads,bank_card_tails,id_card_hand,card_number
	,pKey,wKey,salt,email,account_name,bank_name,bank_account;

	public String getEmail() {
		return email;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_account() {
		return bank_account;
	}

	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer get_id() {
		return _id;
	}
	public void set_id(Integer _id) {
		this._id = _id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getId_card_heads() {
		return id_card_heads;
	}
	public void setId_card_heads(String id_card_heads) {
		this.id_card_heads = id_card_heads;
	}
	public String getIdCard_tails() {
		return idCard_tails;
	}
	public void setIdCard_tails(String idCard_tails) {
		this.idCard_tails = idCard_tails;
	}
	public String getBank_card_heads() {
		return bank_card_heads;
	}
	public void setBank_card_heads(String bank_card_heads) {
		this.bank_card_heads = bank_card_heads;
	}
	public String getBank_card_tails() {
		return bank_card_tails;
	}
	public void setBank_card_tails(String bank_card_tails) {
		this.bank_card_tails = bank_card_tails;
	}
	public String getId_card_hand() {
		return id_card_hand;
	}
	public void setId_card_hand(String id_card_hand) {
		this.id_card_hand = id_card_hand;
	}
	public String getCard_number() {
		return card_number;
	}
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
	public String getPKey() {
		return pKey;
	}
	public void setPKey(String pKey) {
		this.pKey = pKey;
	}
	public String getWKey() {
		return wKey;
	}
	public void setWKey(String wKey) {
		this.wKey = wKey;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
