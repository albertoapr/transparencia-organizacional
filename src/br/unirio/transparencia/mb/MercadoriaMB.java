package br.unirio.transparencia.mb;

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

import br.unirio.transparencia.dao.MercadoriaDAO;
import br.unirio.transparencia.dao.MercadoriaDAOObjectify;
import br.unirio.transparencia.model.Mercadoria;

/**
 * Componente atua como um intermediário das telas do cadastro e os componentes de negócio (<code>DAO</code>) da entidade <code>Mercadoria</code>.
 * 
 * <p>Trata-se de um <code>Managed Bean</code>, ou seja, as instância dessa classe são controladas pelo <code>JSF</code>. Para cada sessão de usuário será criado um objeto <code>MercadoriaMB</code>.</p>
 * 
 * <p>Esse componente atua com um papel parecido com o <code>Controller</code> de outros frameworks <code>MVC</code>, ele resolve o fluxo de navegação e liga os componentes visuais com os dados.</p>
 * 
 * @author YaW Tecnologia
 */
@ManagedBean
@SessionScoped
public class MercadoriaMB implements Serializable {
	
	private static Logger log = Logger.getLogger(MercadoriaMB.class);
	
	/**
	 * Referência do componente de persistência.
	 */
	private MercadoriaDAO dao;
	
	/**
	 * Referência para a mercadoria utiliza na inclusão (nova) ou edição.
	 */
	private Mercadoria mercadoria;
	
	/**
	 * Informação é utilizada na edição da mercadoria, quando a seleção de um registro na listagem ocorrer.
	 */
	private Long idSelecionado;
	
	/**
	 * Mantém as mercadorias apresentadas na listagem indexadas pelo id.
	 * <strong>Importante:</strong> a consulta (query) no DataStore do App Engine pode retornar <i>dados antigos</i>, 
	 * que já foram removidos ou que ainda não foram incluidos, devido a replicação dos dados.
	 * 
	 * Dessa forma esse hashmap mantém um espelho do datastore para minizar o impacto desse modelo do App Engine.
	 */
	private Map<Long, Mercadoria> mercadorias;
	
	public MercadoriaMB() {
		dao = new MercadoriaDAOObjectify();
		fillMercadorias();
	}
	
	public Mercadoria getMercadoria() {
		return mercadoria;
	}
	
	public void setMercadoria(Mercadoria mercadoria) {
		this.mercadoria = mercadoria;
	}
	
	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
	}
	
	public Long getIdSelecionado() {
		return idSelecionado;
	}
	
	/**
	 * @return <code>DataModel</code> para carregar a lista de mercadorias.
	 */
	public DataModel<Mercadoria> getDmMercadorias() {
		return new ListDataModel<Mercadoria>(new ArrayList<Mercadoria>(mercadorias.values()));
	}
	
	private void fillMercadorias() {
		try {
			List<Mercadoria> qryMercadorias = new ArrayList<Mercadoria>(dao.getAll());
			mercadorias = new HashMap<Long, Mercadoria>();
			for (Mercadoria m: qryMercadorias) {
				mercadorias.put(m.getId(), m);
			}
			
			log.debug("Carregou a lista de mercadorias ("+mercadorias.size()+")");
		} catch(Exception ex) {
			log.error("Erro ao carregar a lista de mercadorias.", ex);
			addMessage(getMessageFromI18N("msg.erro.listar.mercadoria"), ex.getMessage());
		}
		
	}
	
	/**
	 * Ação executada quando a página de inclusão de mercadorias for carregada.
	 */
	public void incluir(){
		mercadoria = new Mercadoria();
		log.debug("Pronto pra incluir");
	}
	
	/**
	 * Ação executada quando a página de edição de mercadorias for carregada.
	 */
	public void editar() {
		if (idSelecionado == null) {
			return;
		}
		mercadoria = mercadorias.get(idSelecionado);
		log.debug("Pronto pra editar");
	}

	/**
	 * Operação acionada pela tela de inclusão ou edição, através do <code>commandButton</code> <strong>Salvar</strong>.
	 * @return Se a inclusão/edição foi realizada vai para listagem, senão permanece na mesma tela.
	 */
	public String salvar() {
		try {
			dao.save(mercadoria);
			mercadorias.put(mercadoria.getId(), mercadoria);
		} catch(Exception ex) {
			log.error("Erro ao salvar mercadoria.", ex);
			addMessage(getMessageFromI18N("msg.erro.salvar.mercadoria"), ex.getMessage());
			return "";
		}
		log.debug("Salvour mercadoria "+mercadoria.getId());
		return "listaMercadorias";
	}
	
	/**
	 * Operação acionada pela tela de listagem, através do <code>commandButton</code> <strong>Atualizar</strong>. 
	 */
	public void atualizar() {
		fillMercadorias();
	}
	
	/**
	 * Operação acionada toda a vez que a  tela de listagem for carregada.
	 */
	public void reset() {
		mercadoria = null;
		idSelecionado = null;
	}
	
	/**
	 * Operação acionada pela tela de edição, através do <code>commandButton</code> <strong>Excluir</strong>.
	 * @return Se a exclusão for realizada vai para a listagem, senão permanece na mesma tela.
	 */
	public String remover() {
		try {
			dao.remove(mercadoria);
			mercadorias.remove(mercadoria.getId());
		} catch(Exception ex) {
			log.error("Erro ao remover mercadoria.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.mercadoria"), ex.getMessage());
			return "";
		}
		log.debug("Removeu mercadoria "+mercadoria.getId());
		return "listaMercadorias";
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
