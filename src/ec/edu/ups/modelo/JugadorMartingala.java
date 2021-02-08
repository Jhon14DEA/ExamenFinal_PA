/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ups.modelo;

import ec.edu.ups.vista.Banca;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ASUS
 */


@Entity
@Table(name = "JugadorMartingalas")
@NamedQueries({
    @NamedQuery(name = "JugadorMartingala.findAll", query = "SELECT p FROM JugadorMartingala p")
   })
public class JugadorMartingala extends Jugador {
     @Column(name = "cantidadAApostar")
	private int cantidadAApostar;
      @Column(name = "numeroElegido")
	private int numeroElegido;
	public JugadorMartingala(long saldoInicial, Banca b) {
		super(saldoInicial, b);
		cantidadAApostar=1;
	}

	@Override
	public void comunicarNumero(int numero) {
		if (numero==0){
			System.out.println(nombreHilo + " pierde "+cantidadAApostar);
			cantidadAApostar=cantidadAApostar*2;
			return ;
		}
		if (numeroElegido==numero){
			int beneficios=cantidadAApostar * 36;
			banca.restarSaldo(beneficios);
			sumarSaldo(beneficios);
			cantidadAApostar=1;
		}
		if (numeroElegido!=numero){
			//System.out.println(nombreHilo + " pierde "+cantidadAApostar);
			cantidadAApostar=cantidadAApostar * 2;
		}
		System.out.println(nombreHilo + " queda con un saldo de " + saldo);
		apuestaRealizada=false;
		
	}

	@Override
	public void hacerApuesta() {
		if (!banca.aceptaApuestas()) return ;
		if (apuestaRealizada) return ;
		/* Elegimos del 1 al 36 (no se puede elegir el 0*/
		numeroElegido=1 + generador.nextInt(36);
		
		banca.sumarSaldo(cantidadAApostar);
		restarSaldo(cantidadAApostar);
		apuestaRealizada=true;
		banca.aceptarApuesta(this);
	}
}