    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ups.modelo;

import ec.edu.ups.vista.Banca;
import java.util.ArrayList;
import java.util.Random;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "JugadorParImpares")
@NamedQueries({
    @NamedQuery(name = "JugadorParImpar.findAll", query = "SELECT p FROM JugadorParImpar p")
   })
public class JugadorParImpar extends Jugador{
	public JugadorParImpar(long saldoInicial, Banca b) {
		super(saldoInicial, b);
	}
@Column(name = "jugamosAPares")
	protected boolean jugamosAPares;
	@Override
	public void hacerApuesta() {
		if (!banca.aceptaApuestas()) return ;
		if (apuestaRealizada) return ;
		/* Elegimos una apuesta...*/
		if (generador.nextBoolean()==true){
			//System.out.println(nombreHilo+" elige apostar a par");
			jugamosAPares=true;
		} else {
			//System.out.println(nombreHilo+" elige apostar a impar");
			jugamosAPares=false;
		}
		banca.sumarSaldo(10);
		restarSaldo(10);
		apuestaRealizada=true;
		/* Y pedimos a la banca que nos la acepte*/
		banca.aceptarApuesta(this);
	}

	public boolean esGanador(int num) {
		/* Este jugador necesita comprobar si ha salido el 0,
		 * aunque no lo elige nunca, ya que si hemos apostado
		 * Par podríamos pensar que hemos ganado cuando no es así */
		if (num==0){ 
			return false;
		} else {
			if ((num%2==0 ) && (jugamosAPares))
			{
				return true;
			}
			if ((num%2!=0 ) && (!jugamosAPares))
			{
				return true;
			}
		} //Fin del else externo
		return false;
	//Fin de esGanador
	}

	@Override
	public void comunicarNumero(int numero) {
		
		if ( esGanador(numero) ) {
			/*Ganamos y cogemos a la banca 20 euros*/
			System.out.println(nombreHilo + " gana 20 euros por acertar impar");
			banca.restarSaldo(20);
			this.sumarSaldo(20);
		}
		System.out.println(nombreHilo + " se queda con un saldo de "+saldo);
		/* Sea como sea, al terminar indicamos que ya no tenemos
		 * una apuesta realizada. Es decir, permitirmos al
		 * jugador volver a apostar	 */
		apuestaRealizada=false;
	}
}