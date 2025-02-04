package com.soulcode.goserviceapp.controller;

import com.soulcode.goserviceapp.domain.Servico;
import com.soulcode.goserviceapp.domain.Usuario;
import com.soulcode.goserviceapp.domain.UsuarioLog;
import com.soulcode.goserviceapp.service.ServicoService;
import com.soulcode.goserviceapp.service.UsuarioLogService;
import com.soulcode.goserviceapp.service.UsuarioService;
import com.soulcode.goserviceapp.service.exceptions.ServicoNaoEncontradoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.soulcode.goserviceapp.domain.AgendamentoLog;
import com.soulcode.goserviceapp.service.AgendamentoLogService;
import java.util.List;


@Controller
@RequestMapping(value = "/admin")
public class AdministradorController {
    @Autowired
    private AgendamentoLogService agendamentoLogService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private UsuarioLogService usuarioLogService;

    @GetMapping(value = "/servicos")
    public ModelAndView servicos(@RequestParam(defaultValue = "0", name="page") int pagina,
                                 @RequestParam(name = "search", required = false) String search) {
        ModelAndView mv = new ModelAndView("servicosAdmin");
        try {
            int offset = pagina * 10;
            mv.addObject("currentPage", pagina);
            if (search == null){
                List<Servico> servicos = servicoService.buscarServicosPaginados(offset);
                mv.addObject("servicos", servicos);
                mv.addObject("totalPages", servicos.size());
            } else {
                List<Servico> servicos = servicoService.findServiceByNome(search, offset);
                mv.addObject("servicos", servicos);
                mv.addObject("totalPages", servicos.size());
            }
        } catch (Exception e) {
            mv.addObject("errorMessage", "Erro ao buscar dados de serviços.");
        }
        return mv;
    }

    @PostMapping(value = "/servicos")
    public String createService(Servico servico, RedirectAttributes attributes) {
        try {
            servicoService.createServico(servico);
            attributes.addFlashAttribute("successMessage", "Novo serviço adicionado.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao adicionar novo serviço.");
        }
        return "redirect:/admin/servicos";
    }

    @PostMapping(value = "/servicos/remover")
    public String removeService(@RequestParam(name = "servicoId") Long id, RedirectAttributes attributes) {
        try {
            servicoService.removeServicoById(id);
            attributes.addFlashAttribute("successMessage", "Serviço removido.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao excluir serviço.");
        }
        return "redirect:/admin/servicos";
    }

    @GetMapping(value = "/servicos/editar/{id}")
    public ModelAndView editService(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("editarServico");
        try {
            Servico servico = servicoService.findById(id);
            mv.addObject("servico", servico);
        } catch (ServicoNaoEncontradoException ex) {
            mv.addObject("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados do serviço.");
        }
        return mv;
    }

    @PostMapping(value = "/servicos/editar")
    public String updateService(Servico servico, RedirectAttributes attributes) {
        try {
            servicoService.update(servico);
            attributes.addFlashAttribute("successMessage", "Dados do serviço alterados.");
        } catch (ServicoNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao alterar dados do serviço.");
        }
        return "redirect:/admin/servicos";
    }

    @GetMapping(value = "/usuarios")
    public ModelAndView usuarios(@RequestParam(defaultValue = "0", name="page") int pagina,
                                 @RequestParam(name = "search", required = false) String search) {
        ModelAndView mv = new ModelAndView("usuariosAdmin");
        try {
            int offset = pagina * 10;
            mv.addObject("currentPage", pagina);
            if (search == null){
                List<Usuario> usuarios = usuarioService.buscarUsuariosPaginados(offset);
                mv.addObject("usuarios", usuarios);
                mv.addObject("totalPages", usuarios.size());
            } else {
                List<Usuario> usuarios = usuarioService.findUserByNome(search, offset);
                mv.addObject("usuarios", usuarios);
                mv.addObject("totalPages", usuarios.size());
            }
        } catch (Exception e) {
            mv.addObject("errorMessage", "Erro ao buscar dados de usuários.");
        }
        return mv;
    }

    @PostMapping(value = "/usuarios")
    public String createUser(Usuario usuario, RedirectAttributes attributes) {
        try {
            usuarioService.createUser(usuario);
            attributes.addFlashAttribute("successMessage", "Novo usuário cadastrado.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao cadastrar novo usuário.");
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping(value = "/usuarios/disable")
    public String disableUser(@RequestParam(name = "usuarioId") Long id, RedirectAttributes attributes) {
        try {
            usuarioService.disableUser(id);
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao desabilitar usuário.");
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping(value = "/usuarios/enable")
    public String enableUser(@RequestParam(name = "usuarioId") Long id, RedirectAttributes attributes) {
        try {
            usuarioService.enableUser(id);
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao habilitar usuário.");
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping(value = "/dashboard")
    public ModelAndView dashboard() {
        ModelAndView mv = new ModelAndView("dashboard");
        try {
            List<UsuarioLog> logsAuth = usuarioLogService.findAll();
            mv.addObject("logsAuth", logsAuth);
            List<AgendamentoLog> logsAgendamento = agendamentoLogService.findAll();
            mv.addObject("logsAgendamento", logsAgendamento);
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados de log de autenticação.");
        }
        return mv;
    }
}