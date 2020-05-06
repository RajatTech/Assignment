// ##### HEADER_START #####
// @omName -> amazon_om
// @omDesc -> Amazon_om
// @omIdentifier -> amazon_om
// ##### HEADER_END #####

package com.automatics.packages.objectMap;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import com.automatics.packages.library.*;
import org.openqa.selenium.support.FindAll;

public class amazon_om {
	// ##### OBJECTS_START #####
	
	// ##### PAGE_INFO_START #####
	// PAGE_NAME: Amazon_home_page
	// PAGE_TITLE: Amazon_home_page
	// PAGE_URL: Amazon_home_page
	// PAGE_DESC: Amazon_home_page
	// ##### PAGE_INFO_END ##### 

	// OBJ_DESC: Username
	@FindAll({ @FindBy(xpath = "//noting") })
	public WebElement Amazon_home_page__Username;

	// OBJ_DESC: Tv_65_inch
	@FindAll({ @FindBy(xpath = "//*[@text = '65 inch tv']") })
	public WebElement Amazon_home_page__Tv_65_inch;

	// OBJ_DESC: home
	@FindAll({ @FindBy(xpath = "//android.widget.TextView[@text='Home']") })
	public WebElement Amazon_home_page__home;

	// OBJ_DESC: Search_bar
	@FindAll({ @FindBy(id = "com.amazon.mShop.android.shopping:id/rs_search_src_text") })
	public WebElement Amazon_home_page__Search_bar;
	
	// OBJ_DESC: Search_box
	@FindAll({ @FindBy(xpath = "//*[@text='Search']") })
	public WebElement Amazon_home_page__Search_box;

	// OBJ_DESC: Menu
	@FindAll({ @FindBy(xpath = "//*[@class='android.widget.ImageView']") })
	public WebElement Amazon_home_page__Menu;

	// ##### PAGE_INFO_START #####
	// PAGE_NAME: Amazon_Login_page
	// PAGE_TITLE: Amazon_Login_page
	// PAGE_URL: Amazon_Login_page
	// PAGE_DESC: Amazon_Login_page
	// ##### PAGE_INFO_END ##### 

	// OBJ_DESC: Signin
	@FindAll({ @FindBy(xpath = "//android.widget.Button[@text='Already a customer? Sign in']") })
	public WebElement Amazon_Login_page__Signin;

	// OBJ_DESC: cntinu
	@FindAll({ @FindBy(xpath = "//*[@text='Continue']") })
	public WebElement Amazon_Login_page__cntinu;

	// OBJ_DESC: password
	@FindAll({ @FindBy(xpath = "//*[@resource-id='ap_password']") })
	public WebElement Amazon_Login_page__password;

	// OBJ_DESC: Show_password_checbox
	@FindAll({ @FindBy(xpath = "//*[@text='Show password']") })
	public WebElement Amazon_Login_page__Show_password_checbox;

	// OBJ_DESC: skip_signin
	@FindAll({ @FindBy(xpath = "//*[@text='Skip sign in']") })
	public WebElement Amazon_Login_page__skip_signin;

	// OBJ_DESC: username
	@FindAll({ @FindBy(xpath = "//android.widget.Button[@text='Already a customer? Sign in']") })
	public WebElement Amazon_Login_page__username;

	// OBJ_DESC: Mobile_or_email
	@FindAll({ @FindBy(className = "android.widget.EditText") })
	public WebElement Amazon_Login_page__Mobile_or_email;

	// OBJ_DESC: reference
	@FindAll({ @FindBy(xpath = "//*[@text='avesh_mohammad@yahoo.com']") })
	public WebElement Amazon_Login_page__reference;

	// OBJ_DESC: login
	@FindAll({ @FindBy(xpath = "//*[@resource-id='signInSubmit']") })
	public WebElement Amazon_Login_page__login;

	// ##### PAGE_INFO_START #####
	// PAGE_NAME: Amazon_65_Tv
	// PAGE_TITLE: Amazon_65_Tv
	// PAGE_URL: Amazon_65_Tv
	// PAGE_DESC: Amazon_65_Tv
	// ##### PAGE_INFO_END ##### 

	// OBJ_DESC: n_no_Tv
	@FindAll({ @FindBy(xpath = "(//*[@resource-id='com.amazon.mShop.android.shopping:id/item_title' and @package = 'com.amazon.mShop.android.shopping])[3]") })
	public WebElement Amazon_65_Tv__n_no_Tv;

	// OBJ_DESC: add_to_wishlist
	@FindAll({  })
	public WebElement Amazon_65_Tv__add_to_wishlist;

	// ##### PAGE_INFO_START #####
	// PAGE_NAME: almonds1kg
	// PAGE_TITLE: almonds 1kg
	// PAGE_URL: almonds 1kg
	// PAGE_DESC: almonds1kg
	// ##### PAGE_INFO_END ##### 

	// OBJ_DESC: Add_to_cart
	@FindAll({ @FindBy(xpath = "//*[@text='Add to Cart']") })
	public WebElement almonds1kg__Add_to_cart;

	// OBJ_DESC: Open_cart
	@FindAll({ @FindBy(id = "action_bar_cart_image") })
	public WebElement almonds1kg__Open_cart;

	// OBJ_DESC: Almonds
	@FindAll({ @FindBy(xpath = "//*[@text='Nutraj California Almonds 1Kg']") })
	public WebElement almonds1kg__Almonds;

	// ##### OBJECTS_END #####
	
	WebDriver driver;
	public amazon_om(WebDriver driver){
		Utils.hEnvParams.put("App", this.getClass().getPackage().getName());
		this.driver=driver;
	}
}