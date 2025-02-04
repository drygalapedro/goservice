package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.*;
import com.soulcode.goserviceapp.domain.enums.StatusAgendamento;
import com.soulcode.goserviceapp.repository.AgendamentoLogRepository;
import com.soulcode.goserviceapp.repository.AgendamentoRepository;
import com.soulcode.goserviceapp.repository.UsuarioRepository;
import com.soulcode.goserviceapp.service.exceptions.StatusAgendamentoImutavelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private AgendamentoLogService agendamentoLogService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PrestadorService prestadorService;
    @Autowired
    private AgendamentoLogRepository agendamentoLogRepository;


        @Autowired
        private UsuarioRepository usuarioRepository;



        public Agendamento findById(Long id) {
            Optional<Agendamento> agendamento = agendamentoRepository.findById(id);
            if (agendamento.isPresent()) {
                return agendamento.get();
            }
            throw new RuntimeException("Agendamento não foi encontrado");
        }


    public Agendamento create(Authentication authentication, Long servicoId, Long prestadorId, LocalDate data, LocalTime hora) {
        Cliente cliente = clienteService.findAuthenticated(authentication);
        Prestador prestador = prestadorService.findById(prestadorId);
        Servico servico = servicoService.findById(servicoId);
        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setPrestador(prestador);
        agendamento.setServico(servico);
        agendamento.setData(data);
        agendamento.setHora(hora);

        AgendamentoLog log = new AgendamentoLog();
        log.setCliente(cliente);
        log.setPrestador(prestador);
        log.setServico(servico);
        log.setData(data);
        log.setHora(hora);
        agendamentoLogService.create(log);


        Agendamento agendamentoSaved = agendamentoRepository.save(agendamento);
        agendamentoLog(agendamentoSaved);
        return agendamentoSaved;
    }
    public void agendamentoLog(Agendamento agendamento) {
        try {
            AgendamentoLog agendamentoLog = new AgendamentoLog(agendamento);
            agendamentoLogService.create(agendamentoLog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Cacheable(cacheNames = "redisCache")
    public List<Agendamento> findByCliente(Authentication authentication, int page) {
        System.err.println("BUSCANDO AGENDAMENTOS CLIENTE NO BANCO...");
        Cliente cliente = clienteService.findAuthenticated(authentication);
        return agendamentoRepository.findByClienteEmail(cliente.getEmail(), page);
    }

    public List<Usuario> buscarUsuariosPaginados(int offset) {
        return usuarioRepository.buscaUsuariosPaginados(offset);
   }

    @Cacheable(cacheNames = "redisCache")
    public List<Agendamento> findByPrestador(Authentication authentication, int page) {
        System.err.println("BUSCANDO AGENDAMENTOS PRESTADOR NO BANCO...");
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        return agendamentoRepository.findByPrestadorEmail(prestador.getEmail(), page);
    }

    public void cancelAgendaPrestador(Authentication authentication, Long id) {
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if (agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)) {
            agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO_PELO_PRESTADOR);


            Cliente cliente = agendamento.getCliente();
            Servico servico = agendamento.getServico();
            LocalDate data = agendamento.getData();
            LocalTime hora = agendamento.getHora();
            AgendamentoLog log = new AgendamentoLog();
            log.setStatusAgendamento(StatusAgendamento.CANCELADO_PELO_PRESTADOR);
            log.setCliente(cliente);
            log.setPrestador(prestador);
            log.setServico(servico);
            log.setData(data);
            log.setHora(hora);
            agendamentoLogService.create(log);
            agendamentoRepository.save(agendamento);
            Agendamento agenda = agendamentoRepository.save(agendamento);
            new AgendamentoLog(agenda);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void confirmAgenda(Authentication authentication, Long id) {
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if (agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)) {
            agendamento.setStatusAgendamento(StatusAgendamento.CONFIRMADO);
            Cliente cliente = agendamento.getCliente();
            Servico servico = agendamento.getServico();
            LocalDate data = agendamento.getData();
            LocalTime hora = agendamento.getHora();
            AgendamentoLog log = new AgendamentoLog();
            log.setStatusAgendamento(StatusAgendamento.CONFIRMADO);
            log.setCliente(cliente);
            log.setPrestador(prestador);
            log.setServico(servico);
            log.setData(data);
            log.setHora(hora);
            agendamentoLogService.create(log);
            agendamentoRepository.save(agendamento);
            Agendamento agenda = agendamentoRepository.save(agendamento);
            new AgendamentoLog(agenda);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void cancelAgendaCliente(Authentication authentication, Long id) {
        Cliente cliente = clienteService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if (agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)) {
            agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO_PELO_CLIENTE);

            Prestador prestador = agendamento.getPrestador();
            Servico servico = agendamento.getServico();
            LocalDate data = agendamento.getData();
            LocalTime hora = agendamento.getHora();
            AgendamentoLog log = new AgendamentoLog();
            log.setStatusAgendamento(StatusAgendamento.CANCELADO_PELO_CLIENTE);
            log.setCliente(cliente);
            log.setPrestador(prestador);
            log.setServico(servico);
            log.setData(data);
            log.setHora(hora);
            agendamentoLogService.create(log);
            agendamentoRepository.save(agendamento);
            Agendamento agenda = agendamentoRepository.save(agendamento);
            new AgendamentoLog(agenda);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void completeAgenda(Authentication authentication, Long id) {
        Cliente cliente = clienteService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if (agendamento.getStatusAgendamento().equals(StatusAgendamento.CONFIRMADO)) {
            agendamento.setStatusAgendamento(StatusAgendamento.CONCLUIDO);

            Prestador prestador = agendamento.getPrestador();
            Servico servico = agendamento.getServico();
            LocalDate data = agendamento.getData();
            LocalTime hora = agendamento.getHora();
            AgendamentoLog log = new AgendamentoLog();
            log.setStatusAgendamento(StatusAgendamento.CONCLUIDO);
            log.setCliente(cliente);
            log.setPrestador(prestador);
            log.setServico(servico);
            log.setData(data);
            log.setHora(hora);
            agendamentoLogService.create(log);
            agendamentoRepository.save(agendamento);
            Agendamento agenda = agendamentoRepository.save(agendamento);
            new AgendamentoLog(agenda);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

        public List<Agendamento> findByData(String dataInicio, String dataFim, int page) {
            return agendamentoRepository.findByData(dataInicio, dataFim, page);
        }
    }


