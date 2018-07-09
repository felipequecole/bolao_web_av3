/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.bolao2.views;

import br.ufscar.dc.bolao2.beans.Palpite;
import br.ufscar.dc.bolao2.dao.PalpiteDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author daniel
 */
@Named
@ViewScoped
public class ListaPalpites implements Serializable {

    @Inject
    PalpiteDAO palpiteDao;

    private List<Palpite> palpites;
    private List<Palpite> palpitesFiltrados;
    private List<String> selecoes;

    @PostConstruct
    public void init() {
        try {
            palpites = palpiteDao.listarTodosPalpites();
            selecoes = palpiteDao.listarTodasSelecoes();
        } catch (SQLException ex) {
            Logger.getLogger(ListaPalpites.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Palpite> getPalpites() {
        return palpites;
    }

    public List<Palpite> getPalpitesFiltrados() {
        return palpitesFiltrados;
    }

    public void setPalpitesFiltrados(List<Palpite> palpitesFiltrados) {
        this.palpitesFiltrados = palpitesFiltrados;
    }

    public List<String> getSelecoes() {
        return selecoes;
    }

}
