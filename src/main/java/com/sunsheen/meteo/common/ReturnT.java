package com.sunsheen.meteo.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * rest接口统一数据格式
 * commons return
 * @author wangbiao   2018年7月4日 下午6:01:36
 *
 * @param <T>
 */
public class ReturnT<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int SUCCESS_CODE = HttpStatus.OK.value();
	public static final int FALIED_CODE = HttpStatus.BAD_REQUEST.value();

	private int code;
	private String msg;
	private T data;

	public ReturnT(){}
	public ReturnT(int code, String msg) {
		this.code = code;
		this.msg = msg;
		this.data = null;
	}
	
	public ReturnT(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public ReturnT(String msg, T data) {
		this(SUCCESS_CODE, msg, data);
	}

	public ReturnT(T data) {
		this.code = SUCCESS_CODE;
		this.data = data;
		this.msg = null;
	}

	@JsonIgnore
	public boolean isSuccess(){
		return this.code >= 200 && this.code < 300;
	}

	public int getCode() {
		return code;
	}

	public ReturnT<T> setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public ReturnT<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public T getData() {
		return data;
	}

	public ReturnT<T> setData(T data) {
		this.data = data;
		return this;
	}

	public static ReturnT success(){
		return new ReturnT(SUCCESS_CODE, null);
	}

	public static ReturnT success(String msg){
		return new ReturnT(SUCCESS_CODE, msg);
	}

	public static ReturnT fail(String msg){
		return new ReturnT(FALIED_CODE, msg);
	}
	public static<T> ReturnT fail(String msg, T t){
		return new ReturnT(FALIED_CODE, msg, t);
	}

	public static ReturnT fail(int code, String msg){
		return new ReturnT(code, msg);
	}

	public static<T> ReturnT<T> of(T t){
		return new ReturnT<>(t);
	}

	@Override
	public String toString() {
		return "ReturnT [code=" + code + ", msg=" + msg + ", content=" + data + "]";
	}

}
