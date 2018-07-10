/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.bolao2.servicos;

import br.ufscar.dc.bolao2.beans.Palpite;
import br.ufscar.dc.bolao2.beans.Resultado;
import br.ufscar.dc.bolao2.dao.PalpiteDAO;
import br.ufscar.dc.bolao2.dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author felipequecole
 */
@Stateless
@Path("resultados")
public class ServicosResultado {
    
    @Inject
    PalpiteDAO pdao; 
    
    @Inject
    UsuarioDAO udao; 
    
    int ACERTO_CAMPEAO = 100;
    int ACERTO_VICE = 50;
    int ACERTO_TERCEIRO = 30;
    int ACERTO_QUARTO = 15;


    /**
     * Retrieves representation of an instance of br.ufscar.dc.bolao2.servicos.ResultadosResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }
    
    
     @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculaResultados(Resultado r){
        simularDemora();
        try {
            List<Palpite> palpites = pdao.listarTodosPalpites();
            // logica para calculo
            List<Map<String,String>> ret = calculaResultados(palpites, r);
            return Response.ok(ret).build();
        } catch (SQLException ex) {
            Logger.getLogger(ServicosResultado.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    private List<Map<String,String>> calculaResultados(List<Palpite> palpites, Resultado r){
        List<Map<String,String>> ret = new ArrayList();
        Map<String,String> champ = new HashMap();
        int max = 0; 
        for (Palpite p : palpites){
            int pontuacao = 0; 
            if (p.getCampeao().equals(r.getCampeao())){
                pontuacao += ACERTO_CAMPEAO; 
            }
            if (p.getVice().equals(r.getVice())){
                pontuacao += ACERTO_VICE;
            }
            if (p.getTerceiro().equals(r.getTerceiro())) {
                pontuacao += ACERTO_TERCEIRO;
            }
            if (p.getQuarto().equals(r.getQuarto())){
                pontuacao += ACERTO_QUARTO; 
            }
            if (pontuacao > max){
                max = pontuacao; 
                champ.put("nome", p.getPalpiteiro().getNome());
                champ.put("pontuacao", String.valueOf(pontuacao));
            }
        }
        ret.add(champ);
        return ret; 
    }
    
    private void simularDemora() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServicosPalpite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
