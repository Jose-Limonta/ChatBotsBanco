package com.bots.bots.service.imp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bots.bots.model.Transacciones;
import com.bots.bots.repository.TransaccionesRepository;
import com.bots.bots.service.TransaccionesService;

@Service("servicioTransacciones")
public class TransaccionesServiceImp implements TransaccionesService{
	
	@Autowired
	@Qualifier("transaccionRepository")
	private TransaccionesRepository transaccionRepository;

	@Override
	public List<Transacciones> listAllTransacciones() {
		return (List<Transacciones>) transaccionRepository.findAll();
	}

	@Override
	public Optional<Transacciones> getTransaccionById(Integer id) {
		return transaccionRepository.findById(id);
	}

	@Override
	public Transacciones saveTransaccion(Transacciones transaccion) {
		return transaccionRepository.save(transaccion);
	}

	@Override
	public void deleteTransaccione(Integer id) {
		Optional<Transacciones> transaccion = getTransaccionById(id);
		transaccionRepository.delete(transaccion.get());
		
	}

}
