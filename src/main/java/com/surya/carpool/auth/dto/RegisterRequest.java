package com.surya.carpool.auth.dto;

public class RegisterRequest {

	private String regName;
	private String regEmail;
	private String regPhone;
	private String regPassword;
	private String regConfirmPassword;
	private String regGender;
	private String regDateOfBirth;
	private String regCity;
	private String regState;
	private String regPincode;
	private boolean termsAccepted;
	private boolean privacyAccepted;

	public String getRegName() {
		return regName;
	}

	public void setRegName(String regName) {
		this.regName = regName;
	}

	public String getRegEmail() {
		return regEmail;
	}

	public void setRegEmail(String regEmail) {
		this.regEmail = regEmail;
	}

	public String getRegPhone() {
		return regPhone;
	}

	public void setRegPhone(String regPhone) {
		this.regPhone = regPhone;
	}

	public String getRegPassword() {
		return regPassword;
	}

	public void setRegPassword(String regPassword) {
		this.regPassword = regPassword;
	}

	public String getRegConfirmPassword() {
		return regConfirmPassword;
	}

	public void setRegConfirmPassword(String regConfirmPassword) {
		this.regConfirmPassword = regConfirmPassword;
	}

	public String getRegGender() {
		return regGender;
	}

	public void setRegGender(String regGender) {
		this.regGender = regGender;
	}

	public String getRegDateOfBirth() {
		return regDateOfBirth;
	}

	public void setRegDateOfBirth(String regDateOfBirth) {
		this.regDateOfBirth = regDateOfBirth;
	}

	public String getRegCity() {
		return regCity;
	}

	public void setRegCity(String regCity) {
		this.regCity = regCity;
	}

	public String getRegState() {
		return regState;
	}

	public void setRegState(String regState) {
		this.regState = regState;
	}

	public String getRegPincode() {
		return regPincode;
	}

	public void setRegPincode(String regPincode) {
		this.regPincode = regPincode;
	}

	public boolean isTermsAccepted() {
		return termsAccepted;
	}

	public void setTermsAccepted(boolean termsAccepted) {
		this.termsAccepted = termsAccepted;
	}

	public boolean isPrivacyAccepted() {
		return privacyAccepted;
	}

	public void setPrivacyAccepted(boolean privacyAccepted) {
		this.privacyAccepted = privacyAccepted;
	}
}
