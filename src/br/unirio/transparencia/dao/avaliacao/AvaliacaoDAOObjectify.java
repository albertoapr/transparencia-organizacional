package br.unirio.transparencia.dao.avaliacao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.unirio.transparencia.model.Avaliacao;
import br.unirio.transparencia.model.NivelTransparencia;
import br.unirio.transparencia.model.Organizacao;
import br.unirio.transparencia.model.Profissional;
import br.unirio.transparencia.util.ControladorEstados;
import br.unirio.transparencia.util.TotalizadorAvaliacaoPorNivel;
import br.unirio.transparencia.util.TotalizadorAvaliacoes;

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
		for (Avaliacao avaliacao: avaliacoes){
		  for (NivelTransparencia nivel:NivelTransparencia.values()){
			  TotalizadorAvaliacaoPorNivel totalizador= new TotalizadorAvaliacaoPorNivel(nivel,0);
		     if (avaliacao.getNivelTransparencia()==nivel && avaliacao.getDataValidade().after(hoje)) {
			 	totalizador.setTotal(totalizador.getTotal()+1);
			 	break;
			}
		    listaTotalPorNivel.add(totalizador);
			}
		  
		}
		return listaTotalPorNivel;
	}

	@Override
	/**
	 * Neste método é feita uma busca em todas as avaliações realizadas e é feita uma lista de totalizações por estado
	 */
	public List<TotalizadorAvaliacoes> getTotalizacao(boolean somenteValidas) {
		Date hoje = new Date();
		List<TotalizadorAvaliacoes> listaTotalizacoes =null;
		Map<String,TotalizadorAvaliacoes> totalizacoes = new HashMap<String, TotalizadorAvaliacoes>();
		
		List<Avaliacao> avaliacoes = ofy().load().type(Avaliacao.class).list();
		
		/*Inicializa os totais por estados brasileiros*/
		for (String estado: ControladorEstados.getSiglas())		{
		 if (totalizacoes.get(estado) == null)
			  totalizacoes.put(estado, new TotalizadorAvaliacoes(estado));
		}
		//inicializamos o Totalizador Geral 
		String estado = "Todos";
		if (totalizacoes.get(estado) == null)
		  totalizacoes.put(estado, new TotalizadorAvaliacoes(estado));
		
		for(Avaliacao avaliacao : avaliacoes)
		{
		 if (avaliacao.getDataValidade().after(hoje) || !somenteValidas) //se a avaliação é válida é contabilizada
		 {
		   TotalizadorAvaliacoes totalizador= totalizacoes.get(avaliacao.getOrganizacao().getEstado());
		   TotalizadorAvaliacoes totalizadorGeral= totalizacoes.get("Todos");
		   
		   totalizador.setTotal(totalizador.getTotal()+1);
		   totalizadorGeral.setTotal(totalizadorGeral.getTotal()+1);
		   
		   switch(avaliacao.getNivelTransparencia()){
		   case OPACA:
			   totalizador.setTotalOpaca(totalizador.getTotalOpaca()+1);
			   totalizadorGeral.setTotalOpaca(totalizadorGeral.getTotalOpaca()+1);
               break;
		   case DIVULGADA:
			   totalizador.setTotalDivulgada(totalizador.getTotalDivulgada()+1);
			   totalizadorGeral.setTotalDivulgada(totalizadorGeral.getTotalDivulgada()+1);
			   break;
		   case COMPREENDIDA:
			   totalizador.setTotalCompreendida(totalizador.getTotalCompreendida()+1);
			   totalizadorGeral.setTotalCompreendida(totalizadorGeral.getTotalCompreendida()+1);
			   break;
		   case CONFIAVEL:
			   totalizador.setTotalConfiavel(totalizador.getTotalConfiavel()+1);
			   totalizadorGeral.setTotalConfiavel(totalizadorGeral.getTotalConfiavel()+1);
			   break;
		   case PARTICIPATIVA:
			   totalizador.setTotalParticipativa(totalizador.getTotalParticipativa()+1);
			   totalizadorGeral.setTotalParticipativa(totalizadorGeral.getTotalParticipativa()+1);
			   break;	   
           default:
        	   
		   }
		   totalizacoes.put(avaliacao.getOrganizacao().getEstado(), totalizador);
		   totalizacoes.put("Todos",totalizadorGeral);
		 }
			
		}
		
		listaTotalizacoes =  new ArrayList<TotalizadorAvaliacoes>(totalizacoes.values());
		
		return listaTotalizacoes;
		  
		}
		
	
	
}
