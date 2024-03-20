package com.clase.tarea.service;

import com.clase.tarea.model.Producto;
import com.clase.tarea.model.Response;
import com.clase.tarea.repository.IProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductoServicio {

    @Autowired
    private IProductoRepositorio iProductoRepositorio;

    public List<Producto> obtenerTodosProductos(){
        return iProductoRepositorio.findAll();
    }

    public ResponseEntity <Object> crearProducto(Producto producto){
        iProductoRepositorio.save(producto);

        return ResponseEntity.status(200)
                .body(
                Response.builder()
                        .code(200)
                        .message("Producto agregado")
                        .build()
                );
    }

    public Producto obtenerPorId(Long id){
        return iProductoRepositorio.findById(id)
                .orElse(null);
    }

    public ResponseEntity <Object> actualizarProducto(Long id,Producto producto){
        Producto existeProducto = obtenerPorId(id);
        if(existeProducto != null){
            existeProducto.setNombre(producto.getNombre());
            existeProducto.setMarca(producto.getMarca());
            existeProducto.setPrecio(producto.getPrecio());
            existeProducto.setEstado(producto.getEstado());

            iProductoRepositorio.save(existeProducto);
            return ResponseEntity.status(200)
                    .body(
                            Response.builder()
                                    .code(200)
                                    .message("Producto Actualizado")
                                    .build()
                    );

        }else{
            return ResponseEntity.status(404)
                    .body(
                            Response.builder()
                                    .code(404)
                                    .message("Lo siento, no pudimos actualizar el producto. Parece que el producto no se encontró en nuestra base de datos.")
                                    .build()
                    );
        }
    }
    public void borrarProducto(Long id){
        iProductoRepositorio.deleteById(id);
    }

}
