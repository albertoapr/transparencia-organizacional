package br.unirio.transparencia.controller;

import static javax.faces.context.FacesContext.getCurrentInstance;

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

import br.unirio.transparencia.dao.escopo.EscopoDAO;
import br.unirio.transparencia.dao.escopo.EscopoDAOObjectify;
import br.unirio.transparencia.dao.organizacao.OrganizacaoDAO;
import br.unirio.transparencia.dao.organizacao.OrganizacaoDAOObjectify;
import br.unirio.transparencia.model.Escopo;
import br.unirio.transparencia.model.NivelTransparencia;
import br.unirio.transparencia.model.Organizacao;
import br.unirio.transparencia.util.ControladorEstados;

/**
 * Componente atua como um intermediário das telas do cadastro e os componentes de negócio (<code>DAO</code>) da entidade <code>organizacao</code>.
 * 
 * <p>Trata-se de um <code>Managed Bean</code>, ou seja, as instância dessa classe são controladas pelo <code>JSF</code>. Para cada sessão de usuário será criado um objeto <code>organizacaoMB</code>.</p>
 * 
 * <p>Esse componente atua com um papel parecido com o <code>Controller</code> de outros frameworks <code>MVC</code>, ele resolve o fluxo de navegação e liga os componentes visuais com os dados.</p>
 * 
 * 
 */
@ManagedBean
@SessionScoped
public class EscopoBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
	private static Logger log = Logger.getLogger(EscopoBean.class);
	
	/**
	 * Referência do componente de persistência.
	 */
	private EscopoDAO dao;
	private OrganizacaoBean organizacaoBean;
	
	/**
	 * Referência para a organizacao utiliza na inclusão (nova) ou edição.
	 */
	private Escopo escopo;
	
	/**
	 * Informação é utilizada na edição da organizacao, quando a seleção de um registro na listagem ocorrer.
	 */
	private Long idSelecionado;
	
	/**
	 * Mantém as organizacaos apresentadas na listagem indexadas pelo id.
	 * <strong>Importante:</strong> a consulta (query) no DataStore do App Engine pode retornar <i>dados antigos</i>, 
	 * que já foram removidos ou que ainda não foram incluidos, devido a replicação dos dados.
	 * 
	 * Dessa forma esse hashmap mantém um espelho do datastore para minizar o impacto desse modelo do App Engine.
	 */
	private Map<Long, Escopo> escopos;
	
	public EscopoBean() {
		dao = new EscopoDAOObjectify();
		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
	    this.organizacaoBean =(OrganizacaoBean)FacesContext.getCurrentInstance().getApplication()  
                .getELResolver().getValue(elContext, null, "organizacaoBean");
		fillEscopos();
	}
	

	
	
	public Escopo getEscopo() {
		return escopo;
	}
	
	public void setEscopo(Escopo escopo) {
		this.escopo = escopo;
	}
	
	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
	}
	
	public Long getIdSelecionado() {
		return idSelecionado;
	}
	
	/**
	 * @return <code>DataModel</code> para carregar a lista de organizacaos.
	 */
	public DataModel<Escopo> getDmEscopos() {
		return new ListDataModel<Escopo>(new ArrayList<Escopo>(escopos.values()));
	}
	
	public void fillEscopos() {
		try {
			List<Escopo> qryEscopos ;
			if (organizacaoBean.getOrganizacao()!=null)
			{
			 qryEscopos = new ArrayList<Escopo>(dao.getAll(organizacaoBean.getOrganizacao()));
			}
			else{
			  qryEscopos = new ArrayList<Escopo>(dao.getAll());
			}
			escopos = new HashMap<Long, Escopo>();
			for (Escopo m: qryEscopos) {
				escopos.put(m.getId(), m);
			}
			
			log.debug("Carregou a lista de escopos ("+escopos.size()+")");
		} catch(Exception ex) {
			log.error("Erro ao carregar a lista de escopo.", ex);
			addMessage(getMessageFromI18N("msg.erro.listar.escopo"), ex.getMessage());
		}
		
	}
	
	public Map<Long, Escopo> getEscopos() {
		return escopos;
	}


	public void setEscopos(Map<Long, Escopo> escopos) {
		this.escopos = escopos;
	}


	/**
	 * Ação executada quando a página de inclusão de escopos for carregada.
	 */
	public String incluir(){
		escopo = new Escopo();
		if (organizacaoBean.getOrganizacao()!=null)
		{	
		   escopo.setOrganizacao(organizacaoBean.getOrganizacao());
		   log.debug("Pronto pra incluir");
		   return null;
		}
		else
		{
			addMessage("Nenhuma organização selecionada !", "");
			return "listaOrganizacoes";
			}
		
	}
	
	/**
	 * Ação executada quando a página de edição de escopos for carregada.
	 */
	public void editar() {
		if (idSelecionado == null) {
			return;
		}
		escopo = escopos.get(idSelecionado);
		log.debug("Pronto pra editar");
	}

	/**
	 * Operação acionada pela tela de inclusão ou edição, através do <code>commandButton</code> <strong>Salvar</strong>.
	 * @return Se a inclusão/edição foi realizada vai para listagem, senão permanece na mesma tela.
	 */
	public String salvar() {
		try {
			dao.save(escopo);
		
			escopos.put(escopo.getId(), escopo);
			
			addMessage("Escopo salvo com sucesso !", "");
		} catch(Exception ex) {
			log.error("Erro ao salvar escopo.", ex);
			addMessage(getMessageFromI18N("msg.erro.salvar.escopo"), ex.getMessage());
			return "";
		}
		log.debug("Salvou escopo "+escopo.getId());
		return "listaEscopos";
	
	}
	
	/**
	 * Operação acionada pela tela de listagem, através do <code>commandButton</code> <strong>Atualizar</strong>. 
	 */
	public void atualizar() {
		fillEscopos();
	}
	
	/**
	 * Operação acionada toda a vez que a  tela de listagem for carregada.
	 */
	public void reset() {
		escopo = null;
		idSelecionado = null;
	}
	
	/**
	 * Operação acionada pela tela de edição, através do <code>commandButton</code> <strong>Excluir</strong>.
	 * @return Se a exclusão for realizada vai para a listagem, senão permanece na mesma tela.
	 */
	public String remover() {
		try {
			
			dao.remove(escopo);
			escopos.remove(escopo.getId());
			organizacaoBean.getOrganizacao().removeEscopo(escopo);
			addMessage("Escopo excluído com sucesso !", "");
			log.debug("Removeu escopo "+escopo.getId());
		} catch(Exception ex) {
			log.error("Erro ao remover escopo.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.escopo"), ex.getMessage());
			return "";
		}
		
		return "listaEscopos";
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
