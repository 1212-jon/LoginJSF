package bean.eredua;

import java.io.Serializable;

import businessLogic.BLFacade;
import domain.Seller;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("erregistratuBean")
@RequestScoped
public class ErregistratuBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String izena;
    private String email;
    private String password;

    private BLFacade facadeLogic = FacadeBean.getBusinessLogic();

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String erregistratu() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (facadeLogic == null) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Errorea: ezin izan da negozio-logika eskuratu.", null));
            return "error";
        }

        try {
            String garbiEmail = (email != null) ? email.trim() : null;
            String garbiIzena = (izena != null) ? izena.trim() : null;

            // Kontuz: DataAccess.register-en ordena (email, name, password) da
            Seller sortua = facadeLogic.register(garbiEmail, garbiIzena, password);

            if (sortua != null) {
                // Datu-basean persistitu dela egiaztatzeko login/bilaketa egiaztapena:
                Seller egiaztatua = facadeLogic.isLogin(garbiEmail, password);
                if (egiaztatua != null) {
                    System.out.println("Datu-basean persistituta eta egiaztatuta: " + egiaztatua.getEmail());
                }

                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Erregistraketa ondo egin da eta datu-basean gorde da (" + garbiEmail + ").",
                                null));
                context.getExternalContext().getFlash().setKeepMessages(true);
                return "ok";
            } else {
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Ezin izan da erregistraketa burutu (baliteke email hori jada existitzea).",
                                null));
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Errorea erregistratzean: " + e.getMessage(), null));
            return "error";
        }
    }

    public String atzera() {
        return "atzera";
    }
}