package financas.bb.com.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;

import financas.bb.com.Models.Extrato;
import financas.bb.com.Models.User;
import financas.bb.com.Repository.ExtratoRepositoy;

@Controller
public class ExtratoController {
    private final ExtratoRepositoy er;

    public ExtratoController(ExtratoRepositoy er) {
        this.er = er;
    }

    public List<Extrato> listAll(User user) {
        return er.findByUser(user);
    }
}
