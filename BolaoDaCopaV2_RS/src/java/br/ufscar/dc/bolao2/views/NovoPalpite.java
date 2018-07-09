package br.ufscar.dc.bolao2.views;

import br.ufscar.dc.bolao2.beans.Palpite;
import br.ufscar.dc.bolao2.beans.Usuario;
import br.ufscar.dc.bolao2.dao.PalpiteDAO;
import br.ufscar.dc.bolao2.dao.UsuarioDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class NovoPalpite implements Serializable {

    @Inject
    UsuarioDAO usuarioDao;
    @Inject
    PalpiteDAO palpiteDao;

    Palpite dadosPalpite;
    Usuario usuarioEncontrado;

    NovoPalpiteMaquinaEstados estado;
    MensagemBootstrap mensagem;

    UIInput senhaInput;
    UIInput campeaoInput;
    UIInput dataNascimentoInput;

    public UIInput getDataNascimentoInput() {
        return dataNascimentoInput;
    }

    public void setDataNascimentoInput(UIInput dataNascimentoInput) {
        this.dataNascimentoInput = dataNascimentoInput;
    }
    
    

    public NovoPalpite() {
        recomecar();
    }

    private void recomecar() {
        estado = NovoPalpiteMaquinaEstados.inicio();
        mensagem = new MensagemBootstrap();
        mensagem.setMensagem(true, "Digite seu e-mail para dar início", MensagemBootstrap.TipoMensagem.TIPO_INFO);
        dadosPalpite = new Palpite();
        dadosPalpite.setPalpiteiro(new Usuario());

    }

    public Palpite getDadosPalpite() {
        return dadosPalpite;
    }

    public void setDadosPalpite(Palpite dadosPalpite) {
        this.dadosPalpite = dadosPalpite;
    }

    public MensagemBootstrap getMensagem() {
        return mensagem;
    }

    public NovoPalpiteMaquinaEstados getEstado() {
        return estado;
    }

    public UIInput getCampeaoInput() {
        return campeaoInput;
    }

    public UIInput getSenhaInput() {
        return senhaInput;
    }

    public void setCampeaoInput(UIInput campeaoInput) {
        this.campeaoInput = campeaoInput;
    }

    public void setSenhaInput(UIInput senhaInput) {
        this.senhaInput = senhaInput;
    }
    
    

    public void procurarEmail() {
        simularDemora();
        try {
            usuarioEncontrado = usuarioDao.buscarUsuarioPeloEmail(dadosPalpite.getPalpiteiro().getEmail());
            if (usuarioEncontrado == null) {
                mensagem.setMensagem(true, "E-mail ainda não cadastrado! Informe uma nova senha e demais dados para cadastro", MensagemBootstrap.TipoMensagem.TIPO_INFO);
                estado = NovoPalpiteMaquinaEstados.usuarioInexistente();
            } else {
                mensagem.setMensagem(true, "E-mail já cadastrado! Informe sua senha para enviar o palpite", MensagemBootstrap.TipoMensagem.TIPO_SUCESSO);
                estado = NovoPalpiteMaquinaEstados.usuarioExistente();
            }
        } catch (SQLException ex) {
            Logger.getLogger(NovoPalpite.class.getName()).log(Level.SEVERE, null, ex);
            mensagem.setMensagem(true, "Ocorreu um problema!", MensagemBootstrap.TipoMensagem.TIPO_ERRO);
        }

    }

    public void conferirSenha() {
        simularDemora();
        String senha = (String) dadosPalpite.getPalpiteiro().getSenha();
        if (senha.equals(usuarioEncontrado.getSenha())) {
            dadosPalpite.setPalpiteiro(usuarioEncontrado);
            mensagem.setMensagem(true, "Senha correta! Informe seu palpite!", MensagemBootstrap.TipoMensagem.TIPO_SUCESSO);
            estado = NovoPalpiteMaquinaEstados.usuarioExistenteSenhaCorreta();
        } else {
            estado = NovoPalpiteMaquinaEstados.usuarioExistente();
            mensagem.setMensagem(true, "Senha incorreta! Informe novamente!", MensagemBootstrap.TipoMensagem.TIPO_ERRO);
        }
    }

    public void enviarPalpite() {
        System.out.println("Data de nascimento do componente amarrado: "+dataNascimentoInput.getValue());
        DateFormat df = DateFormat.getInstance();
        System.out.println("Data de nascimento do valor recuperado:" + df.format(dadosPalpite.getPalpiteiro().getDataDeNascimento()));
        simularDemora();
        mensagem.setMensagem(true, "Verifique os dados e confirme o palpite. Atenção, ao confirmar o palpite você concorda em pagar R$ 20,00", MensagemBootstrap.TipoMensagem.TIPO_AVISO);
        if (usuarioEncontrado == null) {
            estado = NovoPalpiteMaquinaEstados.confirmarPalpiteUsuarioInexistente();
        } else {
            estado = NovoPalpiteMaquinaEstados.confirmarPalpiteUsuarioExistente();
        }
    }

    public void confirmarPalpite() {
        simularDemora();
        try {
            Usuario u = usuarioDao.buscarUsuario(dadosPalpite.getPalpiteiro().getId());
            if (u == null) {
                u = usuarioDao.gravarUsuario(dadosPalpite.getPalpiteiro());
            }
            dadosPalpite.getPalpiteiro().setId(u.getId());
            palpiteDao.gravarPalpite(dadosPalpite);

            recomecar();
            mensagem.setMensagem(true, "Seu palpite foi registrado com sucesso! Digite seu e-mail para dar início a outro palpite", MensagemBootstrap.TipoMensagem.TIPO_SUCESSO);

        } catch (SQLException ex) {
            Logger.getLogger(NovoPalpite.class.getName()).log(Level.SEVERE, null, ex);
            mensagem.setMensagem(true, "Ocorreu um problema!", MensagemBootstrap.TipoMensagem.TIPO_ERRO);
        }
    }
    
    public void validarVice(FacesContext context,
            UIComponent toValidate,
            String value) {
        simularDemora();
        String campeao = (String) campeaoInput.getValue();
        if (value.equals(campeao)) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Campeão e vice não podem ser iguais!");
            context.addMessage(toValidate.getClientId(context), message);
        }
    }

    public void validarConfirmacaoDeSenha(FacesContext context,
            UIComponent toValidate,
            String value) {
        simularDemora();
        String senha1 = (String) senhaInput.getValue();
        if (!value.equals(senha1)) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Senha não confere!");
            context.addMessage(toValidate.getClientId(context), message);
        }
    }
    

    private void simularDemora() {
//        // Para testar chamadas AJAX
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(NovoPalpite.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
