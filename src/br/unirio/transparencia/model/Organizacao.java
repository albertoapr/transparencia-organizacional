package br.unirio.transparencia.model;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;

/**
 * Classe de modelo que representa uma organização. A organização é um objeto persistido, por isso utilizamos o nome entidade.
 * 

 * 
 * <p>Essa entidade é mapeada com anotações do <code>Objectify</code>, um framework para persistência alto-nível no datastore (mecanismo de persistência do <code>App Engine</code>).</p>
 * 
 * 
 */
@Entity
public class Organizacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private String nome;
    private String endereco;
	private String estado;
	private List<Key<Escopo>> keysEscopos = new ArrayList<Key<Escopo>>();
	
	@Ignore
	private Map<Long, Escopo> escopos;
	
	public void addEscopo(Escopo escopo){
		if (!keysEscopos.contains(escopo))
		   keysEscopos.add(Key.create(Escopo.class, escopo.getId()));
		else
			keysEscopos.add(keysEscopos.indexOf(escopo), Key.create(Escopo.class, escopo.getId()));
		
		escopos.put(escopo.getId(), escopo);
	}
	
	public void removeEscopo(Escopo escopo){
		keysEscopos.remove(escopo);
		escopos.remove(escopo.getId());
	}
	
	
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}




	
	public Organizacao() {
		escopos = new HashMap<Long, Escopo>();
		Escopo escopo =null;
		
		for (Key<Escopo> k: keysEscopos) {
			escopo = ofy().load().key(k).get();
			escopos.put(escopo.getId(),escopo);
		}
		
		}
		
	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Map<Long, Escopo> getEscopos() {
		return escopos;
	}

	public void setEscopos(Map<Long, Escopo> escopos) {
		this.escopos = escopos;
	}

	public List<Key<Escopo>> getKeysEscopos() {
		return keysEscopos;
	}

	public void setKeysEscopos(List<Key<Escopo>> keysEscopos) {
		this.keysEscopos = keysEscopos;
	}





	
}
