package br.unirio.transparencia.controller;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import br.unirio.transparencia.dao.usuario.UsuarioDAO;
import br.unirio.transparencia.dao.usuario.UsuarioDAOObjectify;
import br.unirio.transparencia.model.TipoUsuario;
import br.unirio.transparencia.model.Usuario;

/**
 * Classe com acoes de login e tratamento de usuarios
 * 
 * 
 */
@ManagedBean
@SessionScoped
public class LoginBean extends BaseBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(LoginBean.class);
	private UsuarioDAO dao;
  	private Usuario usuarioLogado;
  	private String sair = "não"; //controle de logout
  	private boolean autenticado;
  	
  	
  	private boolean checkAdministrador;
  	private ArrayList<String> leiame = this.listaReadme();
	public ArrayList<String> getLeiame() {
		return leiame;
	}
	public void setLeiame(ArrayList<String> leiame) {
		this.leiame = leiame;
	}
	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}
	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	private String user;
	private String password;
	

	public LoginBean() {
			dao = new UsuarioDAOObjectify();
			setAutenticado(false);
			setCheckAdministrador(false);
			
			}
	public String login() 
	{
	
		if (user == null || password == null)
		{
		  this.addErrorMessage("Login ou senha inválidos");
		  log.debug("Login ou senha inválidos");
		  addMessage(getMessageFromI18N("msg.erro.salvar.usuario"), "Login ou senha inválidos");
		  return null;
		}
	   usuarioLogado = dao.findByEmail(user);

		if (usuarioLogado == null)
		{
			  this.addErrorMessage("Login ou senha inválidos");
			  addMessage(getMessageFromI18N("msg.erro.salvar.usuario"), "Login ou senha inválidos");
			  return null;
			}

		if (!usuarioLogado.isActive())
		{
			  this.addErrorMessage("Usuário desabilitado !");
			  addMessage(getMessageFromI18N("msg.erro.salvar.usuario"), "Usuário desabilitado");
			  return null;
			}
	
		if (usuarioLogado.getSenha().compareTo(password) != 0)
		
		{
			  this.addErrorMessage("Usuário ou senha incorretos");
			  addMessage(getMessageFromI18N("msg.erro.salvar.usuario"), "Login ou senha inválidos");
			  return null;
			}
		this.setAutenticado(true);
		
		if (usuarioLogado.getTipo()==TipoUsuario.ADMINISTRADOR)
			this.setCheckAdministrador(true);
		else
			this.setCheckAdministrador(false);
		
		
		return "listaOrganizacoes";
		 
	}


	public String logout()
	{
		
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	
       return "login";
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * @param key
	 * @return Recupera a mensagem do arquivo properties <code>ResourceBundle</code>.
	 */
	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}

	private void addMessage(String summary, String detail) {
		getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
	public boolean isAutenticado() {
		return autenticado;
	}
	public void setAutenticado(boolean autenticado) {
		this.autenticado = autenticado;
	}
	public boolean getCheckAdministrador() {
		return checkAdministrador;
	}
	public void setCheckAdministrador(boolean checkAdministrador) {
		this.checkAdministrador = checkAdministrador;
	}





	
}