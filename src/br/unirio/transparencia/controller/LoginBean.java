package br.unirio.transparencia.controller;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import br.unirio.simplemvc.actions.ActionException;
import br.unirio.simplemvc.utils.Crypto;
import br.unirio.transparencia.dao.usuario.UsuarioDAO;
import br.unirio.transparencia.dao.usuario.UsuarioDAOObjectify;
import br.unirio.transparencia.gae.mail.GerenciadorEmail;
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
	

	
	
	
	/**
	 * Verifica se uma senha � aceit�vel, checando se ela tem pelo menos 8
	 * caracteres, uma letra e um n�mero
	 */
	private boolean senhaAceitavel(String senha)
	{
		return (senha.length() >= 8) && senha.matches(".*[a-zA-Z].*") && senha.matches(".*[0-9].*");
	}

	public String enviaSenha() throws ActionException
	{
		
		if (user == null )
		{
		  this.addErrorMessage("Login inválido");
		  log.debug("Login inválido");
		  addMessage("Erro ao enviar senha", "Login inválido");
		  return null;
		}
	   usuarioLogado = dao.findByEmail(user);

		if (usuarioLogado == null)
		{
			  this.addErrorMessage("Usuário não cadastrado");
			  addMessage("Erro ao enviar senha", "Usuário não cadastrado");
			  return null;
			}

	
		String corpo = "<p>Voc� est� recebendo este e-mail porque pediu a recupera\u00E7\u00E3o da senha de acesso ao sistema de transparencia, sua senha é:  "+ usuarioLogado.getSenha();
		boolean envioOK =false;
		//envioOK=
		GerenciadorEmail gerenciadorEmail = new GerenciadorEmail();
		//GerenciadorEmail.getInstance().envia(usuarioLogado.getNome(), usuarioLogado.getEmail(), "Recuperacao  de acesso ao sistema Transparencia", corpo);
		envioOK = gerenciadorEmail.envia(usuarioLogado.getNome(), usuarioLogado.getEmail(), "Recuperacao  de acesso ao sistema Transparencia", corpo);
		
		//check(envioOK, "Ocorreu um erro ao enviar um e-mail com sua senha");
		if (!envioOK)
		{
		  addMessage("Erro ao enviar senha", "Login ou senha inválidos");
		  return null;
		}
		
		addMessage("Recuperação de senha", "A senha foi enviada para seu email");
	    return null;
	}

	/**
	 * Gera um token para troca de senha
	 */
	private String geraTokenTrocaSenha()
	{
		StringBuilder sb = new StringBuilder();
		Random r = Crypto.createSecureRandom();

		for (int i = 0; i < 256; i++)
		{
			char c = (char) ('A' + r.nextInt(26));
			sb.append(c);
		}

		return sb.toString();
	}





	private boolean enviaSenhaUsuario(Usuario usuario, String senha)
	{
		String corpo = "<p>Para acessar o sistema de inscri��es do PPGI use seu e-mail como login e a senha <b>" + senha + "</b>. ";
		corpo += "O sistema lhe pedir� uma nova senha no primeiro login. Esta senha tamb�m poder� ser trocada posteriormente no ";
		corpo += "sistema.</p>";

		return GerenciadorEmail.getInstance().envia(usuario.getNome(), usuario.getEmail(), "Sua senha de acesso ao sistema do PPGI", corpo);
	}

	
	/**
	 * Gera aleatoriamente a senha de um novo usu�rio
	 */
	private String criaSenhaAleatoria()
	{
		Random r = Crypto.createSecureRandom();
		String senha = "";

		for (int i = 0; i < 3; i++)
			senha = senha + (char) ('A' + r.nextInt(26));

		for (int i = 0; i < 6; i++)
			senha = senha + (char) ('0' + r.nextInt(10));

		return senha;
	}

	
	
	
	
	
	
	
	
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