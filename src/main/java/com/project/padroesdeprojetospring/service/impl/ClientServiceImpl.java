package com.project.padroesdeprojetospring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.padroesdeprojetospring.entity.Adress;
import com.project.padroesdeprojetospring.entity.AdressRepository;
import com.project.padroesdeprojetospring.entity.Client;
import com.project.padroesdeprojetospring.entity.ClientRepository;
import com.project.padroesdeprojetospring.service.ClientService;
import com.project.padroesdeprojetospring.service.ViaCepService;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdressRepository adressRepository;

    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.get();
    }

    @Override
    public void insert(Client client) {
        saveClientWithCep(client);
    }

    @Override
    public void update(Long id, Client client) {
        Optional<Client> clientDb = clientRepository.findById(id);

        if(clientDb.isPresent()){
            saveClientWithCep(client);
        }
    }

    private void saveClientWithCep(Client client) {
        String cep = client.getAdress().getCep();

        Adress adress = adressRepository.findById(cep)
            .orElseGet(() -> {
                Adress newAdress = viaCepService.consultCep(cep);
                adressRepository.save(newAdress);
                return newAdress;
            });
            
        client.setAdress(adress);
        clientRepository.save(client);
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }
    
}
