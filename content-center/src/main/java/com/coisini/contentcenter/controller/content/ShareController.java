package com.coisini.contentcenter.controller.content;

import com.coisini.contentcenter.domain.dto.content.ShareDTO;
import com.coisini.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @author coisini
 * @date
 * @Version 1.0
 */
@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    public final ShareService shareService;

    /**
     * 根据ID查找
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ShareDTO findById(@PathVariable Integer id) {
        return shareService.findById(id);
    }

}
