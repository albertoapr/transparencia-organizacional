package br.unirio.transparencia.controller;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.apache.log4j.Logger;


import br.unirio.transparencia.dao.UsuarioDAO;
import br.unirio.transparencia.dao.UsuarioDAOObjectify;
import br.unirio.transparencia.model.Usuario;

/**
 * Componente atua como um intermediário das telas do cadastro e os componentes de negócio (<code>DAO</code>) da entidade <code>Usuario</code>.
 * 
 * <p>Trata-se de um <code>Managed Bean</code>, ou seja, as instância dessa classe são controladas pelo <code>JSF</code>. Para cada sessão de usuário será criado um objeto <code>UsuarioBean</code>.</p>
 * 
 * <p>Esse componente atua com um papel parecido com o <code>Controller</code> de outros frameworks <code>MVC</code>, ele resolve o fluxo de navegação e liga os componentes visuais com os dados.</p>
 * 
 * @author YaW Tecnologia
 */
@ManagedBean
@SessionScoped
public class UsuarioBean implements Serializable {
	
	private static Logger log = Logger.getLogger(UsuarioBean.class);
	
	/**
	 * Referência do componente de persistência.
	 */
	private UsuarioDAO dao;
	
	/**
	 * Referência para a usuario utiliza na inclusão (nova) ou edição.
	 */
	private Usuario usuario;
	
	/**
	 * Informação é utilizada na edição da usuario, quando a seleção de um registro na listagem ocorrer.
	 */
	private Long idSelecionado;
	
	/**
	 * Mantém as usuarios apresentadas na listagem indexadas pelo id.
	 * <strong>Importante:</strong> a consulta (query) no DataStore do App Engine pode retornar <i>dados antigos</i>, 
	 * que já foram removidos ou que ainda não foram incluidos, devido a replicação dos dados.
	 * 
	 * Dessa forma esse hashmap mantém um espelho do datastore para minizar o impacto desse modelo do App Engine.
	 */
	private Map<Long, Usuario> usuarios;
	
	public UsuarioBean() {
		dao = new UsuarioDAOObjectify();
		fillUsuarios();
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
	}
	
	public Long getIdSelecionado() {
		return idSelecionado;
	}
	
	/**
	 * @return <code>DataModel</code> para carregar a lista de usuarios.
	 */
	public DataModel<Usuario> getDmUsuarios() {
		return new ListDataModel<Usuario>(new ArrayList<Usuario>(usuarios.values()));
	}
	
	private void fillUsuarios() {
		try {
			List<Usuario> qryUsuarios = new ArrayList<Usuario>(dao.getAll());
			usuarios = new HashMap<Long, Usuario>();
			for (Usuario m: qryUsuarios) {
				usuarios.put(m.getId(), m);
			}
			
			log.debug("Carregou a lista de usuarios ("+usuarios.size()+")");
		} catch(Exception ex) {
			log.error("Erro ao carregar a lista de usuarios.", ex);
			addMessage(getMessageFromI18N("msg.erro.listar.usuario"), ex.getMessage());
		}
		
	}
	
	/**
	 * Ação executada quando a página de inclusão de usuarios for carregada.
	 */
	public void incluir(){
		usuario = new Usuario();
		log.debug("Pronto pra incluir");
	}
	
	/**
	 * Ação executada quando a página de edição de usuarios for carregada.
	 */
	public void editar() {
		if (idSelecionado == null) {
			return;
		}
		usuario = usuarios.get(idSelecionado);
		log.debug("Pronto pra editar");
	}

	/**
	 * Operação acionada pela tela de inclusão ou edição, através do <code>commandButton</code> <strong>Salvar</strong>.
	 * @return Se a inclusão/edição foi realizada vai para listagem, senão permanece na mesma tela.
	 */
	public String salvar() {
		try {
			dao.save(usuario);
			usuarios.put(usuario.getId(), usuario);
		} catch(Exception ex) {
			log.error("Erro ao salvar usuario.", ex);
			addMessage(getMessageFromI18N("msg.erro.salvar.usuario"), ex.getMessage());
			return "";
		}
		log.debug("Salvour usuario "+usuario.getId());
		return "listaUsuarios";
	}
	
	/**
	 * Operação acionada pela tela de listagem, através do <code>commandButton</code> <strong>Atualizar</strong>. 
	 */
	public void atualizar() {
		fillUsuarios();
	}
	
	/**
	 * Operação acionada toda a vez que a  tela de listagem for carregada.
	 */
	public void reset() {
		usuario = null;
		idSelecionado = null;
	}
	
	/**
	 * Operação acionada pela tela de edição, através do <code>commandButton</code> <strong>Excluir</strong>.
	 * @return Se a exclusão for realizada vai para a listagem, senão permanece na mesma tela.
	 */
	public String remover() {
		try {
			dao.remove(usuario);
			usuarios.remove(usuario.getId());
		} catch(Exception ex) {
			log.error("Erro ao remover usuario.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.usuario"), ex.getMessage());
			return "";
		}
		log.debug("Removeu usuario "+usuario.getId());
		return "listaUsuarios";
	}
	
	/**
	 * @param key
	 * @return Recupera a mensagem do arquivo properties <code>ResourceBundle</code>.
	 */
	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	/**
	 * Adiciona um mensagem no contexto do Faces (<code>FacesContext</code>).
	 * @param summary
	 * @param detail
	 */
	private void addMessage(String summary, String detail) {
		getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
}
