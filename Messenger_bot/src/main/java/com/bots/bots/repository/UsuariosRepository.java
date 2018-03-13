package com.bots.bots.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bots.bots.model.Usuarios;

@Repository("usuarioRepository")
public interface UsuariosRepository extends CrudRepository<Usuarios, String>{

}
