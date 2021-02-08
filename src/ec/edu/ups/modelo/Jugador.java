/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ups.modelo;

import ec.edu.ups.vista.Banca;
import java.io.Serializable;
import java.util.Random;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.RangePartition;

@Entity
@Table(name = "Jugadores")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQueries({
    @NamedQuery(name = "Jugador.findAll", query = "SELECT p FROM Jugador p"),
    @NamedQuery(name = "Jugador.findByCedula", query = "SELECT p FROM Jugador p WHERE p.codigo = :codigo"),})

public abstract class Jugador implements Runnable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private int codigo;
    @Column(name = "saldo")
    protected long saldo;
    @Column(name = "enBancarrota")
    protected boolean enBancarrota;
    @Column(name = "codigo")
    protected long cantidadApostada;
    @Column(name = "cantidadApostada")
    protected boolean apuestaRealizada;
    @OneToOne
    @JoinColumn(name = "banca_id", nullable = false)
    protected Banca banca;
    @Column(name = "nombreHilo")
    protected String nombreHilo;
    @Column(name = "generador")
    
    protected Random generador;

    public Jugador() {
    }

    public Jugador(long saldoInicial, Banca b) {
        saldo = saldoInicial;
        banca = b;
        apuestaRealizada = false;

        generador = new Random();
    }

    public void sumarSaldo(long cantidad) {
        saldo = saldo + cantidad;
    }

    public void restarSaldo(long cantidad) {
        if (saldo - cantidad <= 0) {
            saldo = 0;
            enBancarrota = true;
            return;
        }
        saldo = saldo - cantidad;
    }

    public boolean enBancarrota() {
        return enBancarrota;
    }

    /* Lo usa la banca para comunicarnos el número*/
    public abstract void comunicarNumero(int numero);

    public abstract void hacerApuesta();

    /* Todos los jugadores hacen lo mismo:
	 * Mientras no estemos en bancarrota ni la
	 * banca tampoco, hacemos apuestas. La banca
	 * nos dirá el número que haya salido y en 
	 * ese momento (y si procede) incrementaremos
	 * nuestro saldo
     */
    @Override
    public void run() {
        nombreHilo = Thread.currentThread().getName();
        while ((!enBancarrota) && (!banca.enBancarrota())) {
            int msAzar;
            /* Mientras la ruleta no acepte apuestas, dormimos un 
			 * periodo al azar */
            while (!banca.aceptaApuestas()) {
                msAzar = this.generador.nextInt(500);
                try {
                    //System.out.println(nombreHilo+":banca ocupada, durmiendo...");
                    Thread.sleep(msAzar);
                } catch (InterruptedException e) {
                    return;
                }
                if (banca.enBancarrota()) {
                    return;
                }
            }

            hacerApuesta();
        }
        String nombre = Thread.currentThread().getName();
        if (enBancarrota) {
            System.out.println(nombre + ": ¡¡Me arruiné!!");
            return;
        }
        if (banca.enBancarrota()) {
            System.out.println(nombre + " hizo saltar la banca");
        }
    }
}
