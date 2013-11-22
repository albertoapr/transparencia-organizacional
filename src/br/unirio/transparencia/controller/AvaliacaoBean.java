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

import com.googlecode.objectify.Ref;

import br.unirio.transparencia.dao.avaliacao.AvaliacaoDAO;
import br.unirio.transparencia.dao.avaliacao.AvaliacaoDAOObjectify;
import br.unirio.transparencia.dao.organizacao.OrganizacaoDAO;
import br.unirio.transparencia.dao.organizacao.OrganizacaoDAOObjectify;
import br.unirio.transparencia.model.Avaliacao;
import br.unirio.transparencia.model.Organizacao;
import br.unirio.transparencia.model.TipoUsuario;

/**
 * Componente atua como um intermediário das telas do cadastro e os componentes de negócio (<code>DAO</code>) da entidade <code>avaliacao</code>.
 * 
 * <p>Trata-se de um <code>Managed Bean</code>, ou seja, as instância dessa classe são controladas pelo <code>JSF</code>. Para cada sessão de usuário será criado um objeto <code>avaliacaoMB</code>.</p>
 * 
 * <p>Esse componente atua com um papel parecido com o <code>Controller</code> de outros frameworks <code>MVC</code>, ele resolve o fluxo de navegação e liga os componentes visuais com os dados.</p>
 * 
 * 
 */
@ManagedBean
@SessionScoped
public class AvaliacaoBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(AvaliacaoBean.class);
	
	/**
	 * Referência do componente de persistência.
	 */
	private AvaliacaoDAO dao;
	
	/**
	 * Referência para a avaliacao utiliza na inclusão (nova) ou edição.
	 */
	private Avaliacao avaliacao;
	
	/**
	 * Informação é utilizada na edição da avaliacao, quando a seleção de um registro na listagem ocorrer.
	 */
	private Long idSelecionado;
	
	/**
	 * Mantém as avaliacaos apresentadas na listagem indexadas pelo id.
	 * <strong>Importante:</strong> a consulta (query) no DataStore do App Engine pode retornar <i>dados antigos</i>, 
	 * que já foram removidos ou que ainda não foram incluidos, devido a replicação dos dados.
	 * 
	 * Dessa forma esse hashmap mantém um espelho do datastore para minizar o impacto desse modelo do App Engine.
	 */
	private Map<Long, Avaliacao> avaliacoes;
	
	public AvaliacaoBean() {
		dao = new AvaliacaoDAOObjectify();
		fillAvaliacoes();
	}
	
	
	public List<Ref<Organizacao>> getOrganizacoes() {
		OrganizacaoDAO dao = new OrganizacaoDAOObjectify();
		List<Ref<Organizacao>> organizacoes = new ArrayList<Ref<Organizacao>>() ;
		for (Organizacao organizacao : dao.getAll() )
		    organizacoes.add(Ref.create(organizacao));
		
		return organizacoes;
	             }
	
	public Avaliacao getAvaliacao() {
		return avaliacao;
	}
	
	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}
	
	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
	}
	
	public Long getIdSelecionado() {
		return idSelecionado;
	}
	
	/**
	 * @return <code>DataModel</code> para carregar a lista de avaliacaos.
	 */
	public DataModel<Avaliacao> getDmAvaliacoes() {
		return new ListDataModel<Avaliacao>(new ArrayList<Avaliacao>(avaliacoes.values()));
	}
	
	private void fillAvaliacoes() {
		try {
			List<Avaliacao> qryAvaliacoes = new ArrayList<Avaliacao>(dao.getAll());
			avaliacoes = new HashMap<Long, Avaliacao>();
			for (Avaliacao m: qryAvaliacoes) {
				avaliacoes.put(m.getId(), m);
			}
			
			log.debug("Carregou a lista de avaliacoes ("+avaliacoes.size()+")");
		} catch(Exception ex) {
			log.error("Erro ao carregar a lista de avaliacoes.", ex);
			addMessage(getMessageFromI18N("msg.erro.listar.avaliacao"), ex.getMessage());
		}
		
	}
	
	/**
	 * Ação executada quando a página de inclusão de avaliacaos for carregada.
	 */
	public void incluir(){
		avaliacao = new Avaliacao();
		log.debug("Pronto pra incluir");
	}
	
	/**
	 * Ação executada quando a página de edição de avaliacaos for carregada.
	 */
	public void editar() {
		if (idSelecionado == null) {
			return;
		}
		avaliacao = avaliacoes.get(idSelecionado);
		log.debug("Pronto pra editar");
	}

	/**
	 * Operação acionada pela tela de inclusão ou edição, através do <code>commandButton</code> <strong>Salvar</strong>.
	 * @return Se a inclusão/edição foi realizada vai para listagem, senão permanece na mesma tela.
	 */
	public String salvar() {
		try {
			avaliacao.setOrganizacaoRef();
		//	avaliacao.setAvaliadorLiderRef();
			dao.save(avaliacao);
			avaliacoes.put(avaliacao.getId(), avaliacao);
		} catch(Exception ex) {
			log.error("Erro ao salvar avaliacao.", ex);
			addMessage(getMessageFromI18N("msg.erro.salvar.avaliacao"), ex.getMessage());
			return "";
		}
		log.debug("Salvour avaliacao "+avaliacao.getId());
		return "listaAvaliacoes";
	
	}
	
	/**
	 * Operação acionada pela tela de listagem, através do <code>commandButton</code> <strong>Atualizar</strong>. 
	 */
	public void atualizar() {
		fillAvaliacoes();
	}
	
	/**
	 * Operação acionada toda a vez que a  tela de listagem for carregada.
	 */
	public void reset() {
		avaliacao = null;
		idSelecionado = null;
	}
	
	/**
	 * Operação acionada pela tela de edição, através do <code>commandButton</code> <strong>Excluir</strong>.
	 * @return Se a exclusão for realizada vai para a listagem, senão permanece na mesma tela.
	 */
	public String remover() {
		try {
			dao.remove(avaliacao);
			avaliacoes.remove(avaliacao.getId());
		} catch(Exception ex) {
			log.error("Erro ao remover avaliacao.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.avaliacao"), ex.getMessage());
			return "";
		}
		log.debug("Removeu avaliacao "+avaliacao.getId());
		return "listaAvaliacoes";
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
