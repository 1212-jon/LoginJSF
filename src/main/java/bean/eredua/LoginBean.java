package bean.eredua;

import java.io.Serializable;

import businessLogic.BLFacade;
import domain.Seller;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private Seller seller;

    private BLFacade facadeLogic = FacadeBean.getBusinessLogic();

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

    public Seller getSeller() {
        return seller;
    }

    public String sartu() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (facadeLogic == null) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Errorea: ezin izan da negozio-logika (BLFacade) eskuratu.", null));
            return "error";
        }

        try {
            String garbiEmail = (email != null) ? email.trim() : null;
            Seller s = facadeLogic.isLogin(garbiEmail, password);

            if (s != null) {
                this.seller = s;
                System.out.println("Saioa hasita datu-baseko erabiltzailearekin: " + s.getEmail());
                return "ok";
            } else {
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Posta elektronikoa edo pasahitza ez dira zuzenak.", null));
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Errorea saioa hastean: " + e.getMessage(), null));
            return "error";
        }
    }

    public String irten() {
        this.seller = null;
        this.password = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "irten";
    }
}