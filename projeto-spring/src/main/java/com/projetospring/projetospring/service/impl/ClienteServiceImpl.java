package com.projetospring.projetospring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.projetospring.projetospring.model.Cliente;
import com.projetospring.projetospring.model.ClienteRepository;
import com.projetospring.projetospring.model.Endereco;
import com.projetospring.projetospring.model.EnderecoRepository;
import com.projetospring.projetospring.service.ClienteService;
import com.projetospring.projetospring.service.ViaCepService;

public class ClienteServiceImpl implements ClienteService{

	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private ViaCepService viaCepService;
	
	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		if(clienteRepository.findById(id) == null) {
			return null;
		}
		return cliente.get();
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	private void salvarClienteComCep(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		
		clienteRepository.save(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		Optional<Cliente> clienteBd = clienteRepository.findById(id);
		if(clienteBd.isPresent()) {
			salvarClienteComCep(cliente);
		}
	}

	@Override
	public void deletar(Long id) {
		clienteRepository.deleteById(id);
	}

}
