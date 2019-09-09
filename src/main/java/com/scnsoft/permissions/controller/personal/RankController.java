package com.scnsoft.permissions.controller.personal;

import com.scnsoft.permissions.dto.personal.RankDTO;
import com.scnsoft.permissions.service.personal.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController(value = "ranks")
@RequestMapping("ranks")
@RequiredArgsConstructor
public class RankController {
    private final RankService rankService;

    @GetMapping(path = "vote")
    public RankDTO vote(@RequestParam Long userId, @RequestParam boolean isLike) {
        return rankService.vote(userId, isLike);
    }

    @GetMapping(value = "{id}")
    public RankDTO getUserRank(@PathVariable(value = "id") Long userId) {
        return rankService.findById(userId).orElseThrow(RuntimeException::new);
    }
}