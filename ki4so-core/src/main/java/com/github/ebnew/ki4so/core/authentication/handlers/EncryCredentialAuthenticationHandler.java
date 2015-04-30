package com.github.ebnew.ki4so.core.authentication.handlers;

import com.github.ebnew.ki4so.core.authentication.KnightCredential;
import com.github.ebnew.ki4so.core.authentication.KnightEncryCredential;
import com.github.ebnew.ki4so.core.authentication.KnightEncryCredentialManager;
import com.github.ebnew.ki4so.core.model.KnightCredentialInfo;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.ebnew.ki4so.core.exception.AuthenticationException;
import com.github.ebnew.ki4so.core.exception.InvalidEncryCredentialException;

/**
 * 认证后的凭据认证处理器实现类，需要验证认证后的凭据是否有效，凭据是否过期等等其它
 * 合法性验证。
 * @author burgess yang
 *
 */
public class EncryCredentialAuthenticationHandler extends
		AbstractPreAndPostProcessingAuthenticationHandler {
	
	@Autowired
	private KnightEncryCredentialManager encryCredentialManager;
	
	public void setEncryCredentialManager(
            KnightEncryCredentialManager encryCredentialManager) {
		this.encryCredentialManager = encryCredentialManager;
	}

	/** Default class to support if one is not supplied. */
	private static final Class<KnightEncryCredential> DEFAULT_CLASS = KnightEncryCredential.class;




    @Override
    protected boolean doAuthentication(KnightCredential credential) throws AuthenticationException {
        //不支持的凭据直接返回false.
        if(!this.supports(credential)){
            return false;
        }
        if(credential!=null && credential instanceof KnightEncryCredential){
            KnightEncryCredential encryCredential = (KnightEncryCredential)credential;
            try{
                //解密凭据信息。
                KnightCredentialInfo encryCredentialInfo = this.encryCredentialManager.decrypt(encryCredential);
                //设置凭据信息的关联性。
                if(encryCredentialInfo!=null){
                    encryCredential.setCredentialInfo(encryCredentialInfo);
                    //检查加密凭据的合法性。
                    return this.encryCredentialManager.checkEncryCredentialInfo(encryCredentialInfo);
                }
            }catch (InvalidEncryCredentialException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * @return true if the credentials are not null and the credentials class is
     *         equal to the class defined in classToSupport.
     */
    @Override
    public boolean supports(KnightCredential credential) {
        return credential != null
                && (DEFAULT_CLASS.equals(credential.getClass()) || (DEFAULT_CLASS
                .isAssignableFrom(credential.getClass())));
    }
}
