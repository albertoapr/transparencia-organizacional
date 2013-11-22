/**
 * 
 */
package br.unirio.transparencia.controller;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.apache.log4j.Logger;

import br.unirio.transparencia.dao.profissional.ProfissionalDAO;
import br.unirio.transparencia.dao.profissional.ProfissionalDAOObjectify;
import br.unirio.transparencia.model.Profissional;
import br.unirio.transparencia.model.TipoUsuario;

/**
 * @author alberto
 *
 */
@ManagedBean
@SessionScoped
public class ProfissionalBean implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private static Logger log = Logger.getLogger(ProfissionalBean.class);
	
	/**
	 * Referência do componente de persistência.
	 */
	
	
	private ProfissionalDAO dao;
	
	private Boolean modoEdicao;
	private LoginBean loginBean;


	/**
	 * Referência para a profissional utiliza na inclusão (nova) ou edição.
	 */
	private Profissional profissional;
	
	/**
	 * Informação é utilizada na edição da profissional, quando a seleção de um registro na listagem ocorrer.
	 */
	private Long idSelecionado;
	
	/**
	 * Mantém as profissionais apresentadas na listagem indexadas pelo id.
	 * <strong>Importante:</strong> a consulta (query) no DataStore do App Engine pode retornar <i>dados antigos</i>, 
	 * que já foram removidos ou que ainda não foram incluidos, devido a replicação dos dados.
	 * 
	 * Dessa forma esse hashmap mantém um espelho do datastore para minizar o impacto desse modelo do App Engine.
	 */
	private Map<Long, Profissional> profissionais;
	
	public ProfissionalBean() {
		dao = new ProfissionalDAOObjectify();
		fillProfissionais();
		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();  
		this.loginBean =(LoginBean)FacesContext.getCurrentInstance().getApplication()  
                         .getELResolver().getValue(elContext, null, "loginBean"); 
	}
	
	public Profissional getProfissional() {
		return profissional;
	}
	
	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}
	
	
	
	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
	}
	
	public Long getIdSelecionado() {
		return idSelecionado;
	}
	
	/**
	 * @return <code>DataModel</code> para carregar a lista de profissionais.
	 */
	public DataModel<Profissional> getDmProfissionais() {
	
		return new ListDataModel<Profissional>(new ArrayList<Profissional>(profissionais.values()));
	}

	private void fillProfissionais() {
		
		try {
			
			List<Profissional> qryProfissionais = new ArrayList<Profissional>(dao.getAll());
			profissionais = new HashMap<Long, Profissional>();
			for (Profissional m: qryProfissionais) {
				profissionais.put(m.getId(), m);
				
			}
			
			
			log.debug("Carregou a lista de profissionais ("+profissionais.size()+")");
		} catch(Exception ex) {
			log.error("Erro ao carregar a lista de profissionais.", ex);
			addMessage(getMessageFromI18N("msg.erro.listar.profissional"), ex.getMessage());
		}
		
	}
	
	/**
	 * Ação executada quando a página de inclusão de profissionais for carregada.
	 */
	public void incluir(){
		profissional = new Profissional();
		log.debug("Pronto pra incluir");
		setModoEdicao(false);
	}
	
	/**
	 * Ação executada quando a página de edição de profissionais for carregada.
	 */
	public void editar() {
		if (idSelecionado == null) {
			return;
		}
		profissional = profissionais.get(idSelecionado);
		setModoEdicao(true);
		log.debug("Pronto pra editar");
	}

	/**
	 * Operação acionada pela tela de inclusão ou edição, através do <code>commandButton</code> <strong>Salvar</strong>.
	 * @return Se a inclusão/edição foi realizada vai para listagem, senão permanece na mesma tela.
	 * @throws IOException 
	 */
	
	public boolean checkAutenticado(){
		return (loginBean.isAutenticado());	
					
	}
	
	public boolean checkPermissao() {
		if (loginBean.getUsuarioLogado()!=null)
		  return (loginBean.getUsuarioLogado().getTipo() == TipoUsuario.ADMINISTRADOR) ;
		else
		return false;
		}
	
	public String checkList(){
		if (!checkAutenticado()){addMessage("Login necessário para este conteúdo !", "");
			
			}
		if (!checkPermissao()){
			addMessage("Você não possui as permissões necessárias", "");
			
			}
		
		return "";
	}
	
	public String salvar() {
	if(!getModoEdicao())	
		
		try {
		
			dao.save(profissional);
			profissionais.put(profissional.getId(), profissional);
		} catch(Exception ex) {
			log.error("Erro ao salvar profissional.", ex);
			addMessage(getMessageFromI18N("msg.erro.salvar.profissional"), ex.getMessage());
			return "";
		}
		log.debug("Salvou profissional "+profissional.getId());
		addMessage("Profissional salvo com sucesso !", "");
		return "listaProfissionais";
	}
	
	/**
	 * Operação acionada pela tela de listagem, através do <code>commandButton</code> <strong>Atualizar</strong>. 
	 */
	public void atualizar() {
		fillProfissionais();
	}
	
	/**
	 * Operação acionada toda a vez que a  tela de listagem for carregada.
	 */
	public void reset() {
		
			
		profissional = null;
		idSelecionado = null;
	}
	
	/**
	 * Operação acionada pela tela de edição, através do <code>commandButton</code> <strong>Excluir</strong>.
	 * @return Se a exclusão for realizada vai para a listagem, senão permanece na mesma tela.
	 */
	public String remover() {
		try {
			dao.remove(profissional);
			profissionais.remove(profissional.getId());
		} catch(Exception ex) {
			log.error("Erro ao remover profissional.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.profissional"), ex.getMessage());
			return "";
		}
		log.debug("Removeu profissional "+profissional.getId());
		return "listaProfissionais";
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

	public Boolean getModoEdicao() {
		return modoEdicao;
	}

	public void setModoEdicao(Boolean modoEdicao) {
		this.modoEdicao = modoEdicao;
	}
}
