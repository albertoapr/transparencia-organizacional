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

import br.unirio.transparencia.dao.organizacao.OrganizacaoDAO;
import br.unirio.transparencia.dao.organizacao.OrganizacaoDAOObjectify;
import br.unirio.transparencia.model.Organizacao;

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
public class OrganizacaoBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   // private List<Organizacao> organizacoes;
	private static Logger log = Logger.getLogger(OrganizacaoBean.class);
	
	/**
	 * Referência do componente de persistência.
	 */
	private OrganizacaoDAO dao;
	
	/**
	 * Referência para a organizacao utiliza na inclusão (nova) ou edição.
	 */
	private Organizacao organizacao;
	
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
	private Map<Long, Organizacao> organizacoes;
	
	public OrganizacaoBean() {
		dao = new OrganizacaoDAOObjectify();
		fillOrganizacoes();
	}
	
	
	public Organizacao getOrganizacao() {
		return organizacao;
	}
	
	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
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
	public DataModel<Organizacao> getDmOrganizacoes() {
		return new ListDataModel<Organizacao>(new ArrayList<Organizacao>(organizacoes.values()));
	}
	
	private void fillOrganizacoes() {
		try {
			List<Organizacao> qryOrganizacoes = new ArrayList<Organizacao>(dao.getAll());
			organizacoes = new HashMap<Long, Organizacao>();
			for (Organizacao m: qryOrganizacoes) {
				organizacoes.put(m.getId(), m);
			}
			
			log.debug("Carregou a lista de organizacoes ("+organizacoes.size()+")");
		} catch(Exception ex) {
			log.error("Erro ao carregar a lista de organizacoes.", ex);
			addMessage(getMessageFromI18N("msg.erro.listar.organizacao"), ex.getMessage());
		}
		
	}
	
	/**
	 * Ação executada quando a página de inclusão de organizacaos for carregada.
	 */
	public void incluir(){
		organizacao = new Organizacao();
		log.debug("Pronto pra incluir");
	}
	
	/**
	 * Ação executada quando a página de edição de organizacaos for carregada.
	 */
	public void editar() {
		if (idSelecionado == null) {
			return;
		}
		organizacao = organizacoes.get(idSelecionado);
		log.debug("Pronto pra editar");
	}

	/**
	 * Operação acionada pela tela de inclusão ou edição, através do <code>commandButton</code> <strong>Salvar</strong>.
	 * @return Se a inclusão/edição foi realizada vai para listagem, senão permanece na mesma tela.
	 */
	public String salvar() {
		try {
			dao.save(organizacao);
			organizacoes.put(organizacao.getId(), organizacao);
		} catch(Exception ex) {
			log.error("Erro ao salvar organizacao.", ex);
			addMessage(getMessageFromI18N("msg.erro.salvar.organizacao"), ex.getMessage());
			return "";
		}
		log.debug("Salvour organizacao "+organizacao.getId());
		return "listaOrganizacoes";
	
	}
	
	/**
	 * Operação acionada pela tela de listagem, através do <code>commandButton</code> <strong>Atualizar</strong>. 
	 */
	public void atualizar() {
		fillOrganizacoes();
	}
	
	/**
	 * Operação acionada toda a vez que a  tela de listagem for carregada.
	 */
	public void reset() {
		organizacao = null;
		idSelecionado = null;
	}
	
	/**
	 * Operação acionada pela tela de edição, através do <code>commandButton</code> <strong>Excluir</strong>.
	 * @return Se a exclusão for realizada vai para a listagem, senão permanece na mesma tela.
	 */
	public String remover() {
		try {
			dao.remove(organizacao);
			organizacoes.remove(organizacao.getId());
		} catch(Exception ex) {
			log.error("Erro ao remover organizacao.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.organizacao"), ex.getMessage());
			return "";
		}
		log.debug("Removeu organizacao "+organizacao.getId());
		return "listaOrganizacoes";
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
