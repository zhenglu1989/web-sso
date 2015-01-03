package com.github.ebnew.ki4so.core.exception;

/**
 * 参数未初始化异常
 * @author Huiqiang Lai
 *
 */
public class ParamsNotInitiatedCorrectly extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParamsNotInitiatedCorrectly(String message){
		super(message);
	};
}
