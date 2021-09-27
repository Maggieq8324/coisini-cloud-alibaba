package com.coisini.contentcenter.controller.content;

//import com.coisini.contentcenter.auth.CheckAuthorization;
import com.coisini.contentcenter.domain.dto.content.ShareAuditDTO;
import com.coisini.contentcenter.domain.entity.content.Share;
import com.coisini.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareAdminController {

    private final ShareService shareService;


    /**
     * 管理员审核分享
     * @param id
     * @param auditDTO
     * @return
     */
    @PutMapping("/audit/{id}")
//    @CheckAuthorization("admin")
    public Share auditById(@PathVariable Integer id, @RequestBody ShareAuditDTO auditDTO) {
        return shareService.auditById(id, auditDTO);
    }

}

