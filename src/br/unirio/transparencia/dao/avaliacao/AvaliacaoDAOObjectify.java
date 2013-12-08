package br.unirio.transparencia.dao.avaliacao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import br.unirio.transparencia.model.Avaliacao;
import br.unirio.transparencia.util.TotalizadorAvaliacaoPorEstado;
import br.unirio.transparencia.util.TotalizadorAvaliacaoPorNivel;
import br.unirio.transparencia.model.NivelTransparencia;
import br.unirio.transparencia.model.Organizacao;
import br.unirio.transparencia.model.Profissional;
import br.unirio.transparencia.util.ControladorEstados;

import com.googlecode.objectify.Key;

public class AvaliacaoDAOObjectify implements Serializable, AvaliacaoDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Long save(Avaliacao avaliacao) {
		//setando as chaves de referência para tabelas externas
		Key<Profissional> keyAvaliador =Key.create(Profissional.class, avaliacao.getAvaliador().getId());
		Key<Organizacao> keyOrganizacao =Key.create(Organizacao.class, avaliacao.getOrganizacao().getId());
		
		avaliacao.setKeyAvaliador(keyAvaliador);
		avaliacao.setKeyOrganizacao(keyOrganizacao);
		ofy().save().entity(avaliacao).now();
		return avaliacao.getId();
	}
	
	@Override
	public List<Avaliacao> getAll() {
		return ofy().load().type(Avaliacao.class).list();
	}
	
	@Override
	public Boolean remove(Avaliacao avaliacao) {
		ofy().delete().entity(avaliacao).now();
		return true;
	}
	
	@Override
	public Avaliacao findById(Long id) {
		Key<Avaliacao> k = Key.create(Avaliacao.class, id);
		return ofy().load().key(k).get();
	}

	@Override
	public List<TotalizadorAvaliacaoPorNivel> getTotalPorNivel() {
		Date hoje = new Date();
		List<TotalizadorAvaliacaoPorNivel> listaTotalPorNivel = new ArrayList<TotalizadorAvaliacaoPorNivel>();
		List<Avaliacao> avaliacoes = ofy().load().type(Avaliacao.class).list();
		for (NivelTransparencia nivel:NivelTransparencia.values())
		{
		  TotalizadorAvaliacaoPorNivel totalizador= new TotalizadorAvaliacaoPorNivel(nivel,0);
		  for (Avaliacao avaliacao: avaliacoes){
		    if (avaliacao.getNivelTransparencia()==nivel && avaliacao.getDataValidade().after(hoje)) {
			 	totalizador.setTotal(totalizador.getTotal()+1);	
			}
			}
		  listaTotalPorNivel.add(totalizador);
		}
		return listaTotalPorNivel;
	}

	@Override
	/**
	 * Neste método é feita uma busca em todas as avaliações realizadas e é feita uma lista de totalizações por estado
	 */
	public List<TotalizadorAvaliacaoPorEstado> getTotalPorEstado() {
		Date hoje = new Date();
		List<TotalizadorAvaliacaoPorEstado> listaTotalizacoes =null;
		Map<String,TotalizadorAvaliacaoPorEstado> totalizacoes = new HashMap<String, TotalizadorAvaliacaoPorEstado>();
		
		List<Avaliacao> avaliacoes = ofy().load().type(Avaliacao.class).list();
		/*Inicializa os totalizadores de estado*/
		for (String estado: ControladorEstados.getSiglas())
		{
		 if (totalizacoes.get(estado) == null)
			  totalizacoes.put(estado, new TotalizadorAvaliacaoPorEstado(estado,0));
		}
		
		for(Avaliacao avaliacao : avaliacoes)
		{
		 if (avaliacao.getDataValidade().after(hoje)) //se a avaliação é válida é contabilizada
		 {
		   TotalizadorAvaliacaoPorEstado totalizador= totalizacoes.get(avaliacao.getOrganizacao().getEstado());
		   totalizador.setTotal(totalizador.getTotal()+1);
		   totalizacoes.put(avaliacao.getOrganizacao().getEstado(), totalizador);
		 }
			
		}
		
		listaTotalizacoes =  new ArrayList<TotalizadorAvaliacaoPorEstado>(totalizacoes.values());
		
		return listaTotalizacoes;
		  
		}
		
	
	
}
