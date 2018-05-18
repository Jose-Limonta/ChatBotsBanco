package com.bots.bots.service.imp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bots.bots.model.Tarjetas;
import com.bots.bots.repository.TarjetasRepository;
import com.bots.bots.service.TarjetasService;

@Service("servicioTarjetas")
public class TarjetasServiceImp implements TarjetasService{
	
	@Autowired
	@Qualifier("tarjetaRepository")
	private TarjetasRepository tarjetaRepository;

	@Override
	public List<Tarjetas> listAllTarjetas() {
		return (List<Tarjetas>) tarjetaRepository.findAll();
	}

	@Override
	public Optional<Tarjetas> getTarjetaById(String idtarjeta) {
		return tarjetaRepository.findById(idtarjeta);
	}

	@Override
	public Tarjetas saveTarjeta(Tarjetas tarjeta) {
		return tarjetaRepository.save(tarjeta);
	}

	@Override
	public void deleteTarjetas(String idtarjeta) {
		Optional<Tarjetas> tarjeta = getTarjetaById(idtarjeta);
		if(tarjeta.isPresent()) {
			tarjetaRepository.delete(tarjeta.get());
		}
	}

}
