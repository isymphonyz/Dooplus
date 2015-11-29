package com.keng.dooplus.thetv.model;

public class NavDrawerItem {
	
	private String title;
	private int icon;
	private String contentImg;
	private String count = "0";
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;
	private boolean isClickable = false;
	
	public NavDrawerItem(){}

	public NavDrawerItem(String title, int icon){
		this.title = title;
		this.icon = icon;
	}

	public NavDrawerItem(String title, String contentImg){
		this.title = title;
		this.contentImg = contentImg;
	}
	
	public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count, boolean isClickable){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
		this.isClickable = isClickable;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public int getIcon(){
		return this.icon;
	}
	
	public String getContentImage(){
		return this.contentImg;
	}
	
	public String getCount(){
		return this.count;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public boolean getClickable(){
		return this.isClickable;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	public void setContentImage(String contentImg){
		this.contentImg = contentImg;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
	
	public void setClickable(boolean isClickable){
		this.isClickable = isClickable;
	}
}
