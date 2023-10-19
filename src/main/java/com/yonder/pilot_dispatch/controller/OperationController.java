package com.yonder.pilot_dispatch.controller;

import com.yonder.pilot_dispatch.domain.Airplane;
import com.yonder.pilot_dispatch.domain.Unit;
import com.yonder.pilot_dispatch.model.OperationDTO;
import com.yonder.pilot_dispatch.repos.AirplaneRepository;
import com.yonder.pilot_dispatch.repos.UnitRepository;
import com.yonder.pilot_dispatch.service.OperationService;
import com.yonder.pilot_dispatch.util.CustomCollectors;
import com.yonder.pilot_dispatch.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/operations")
public class OperationController {

    private final OperationService operationService;
    private final AirplaneRepository airplaneRepository;
    private final UnitRepository unitRepository;

    public OperationController(final OperationService operationService,
            final AirplaneRepository airplaneRepository, final UnitRepository unitRepository) {
        this.operationService = operationService;
        this.airplaneRepository = airplaneRepository;
        this.unitRepository = unitRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("airplaneValues", airplaneRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Airplane::getId, Airplane::getName)));
        model.addAttribute("unitValues", unitRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Unit::getId, Unit::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("operations", operationService.findAll());
        return "operation/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("operation") final OperationDTO operationDTO) {
        return "operation/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("operation") @Valid final OperationDTO operationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") && operationService.nameExists(operationDTO.getName())) {
            bindingResult.rejectValue("name", "Exists.operation.name");
        }
        if (bindingResult.hasErrors()) {
            return "operation/add";
        }
        operationService.create(operationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("operation.create.success"));
        return "redirect:/operations";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("operation", operationService.get(id));
        return "operation/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("operation") @Valid final OperationDTO operationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final OperationDTO currentOperationDTO = operationService.get(id);
        if (!bindingResult.hasFieldErrors("name") &&
                !operationDTO.getName().equalsIgnoreCase(currentOperationDTO.getName()) &&
                operationService.nameExists(operationDTO.getName())) {
            bindingResult.rejectValue("name", "Exists.operation.name");
        }
        if (bindingResult.hasErrors()) {
            return "operation/edit";
        }
        operationService.update(id, operationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("operation.update.success"));
        return "redirect:/operations";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = operationService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            operationService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("operation.delete.success"));
        }
        return "redirect:/operations";
    }

}
