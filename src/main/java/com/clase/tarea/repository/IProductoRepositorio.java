package com.clase.tarea.repository;

import com.clase.tarea.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductoRepositorio extends JpaRepository<Producto, Long> {

}
