package com.hong.ForPaw.controller;

import com.hong.ForPaw.controller.DTO.GroupRequest;
import com.hong.ForPaw.controller.DTO.GroupResponse;
import com.hong.ForPaw.core.security.CustomUserDetails;
import com.hong.ForPaw.core.utils.ApiUtils;
import com.hong.ForPaw.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/groups")
    public ResponseEntity<?> createGroup(@RequestBody GroupRequest.CreateGroupDTO requestDTO, @AuthenticationPrincipal CustomUserDetails userDetails){

        groupService.createGroup(requestDTO, userDetails.getUser().getId());
        return ResponseEntity.ok().body(ApiUtils.success(HttpStatus.OK, null));
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<?> findGroupById(@PathVariable Long groupId, @AuthenticationPrincipal CustomUserDetails userDetails){

        GroupResponse.FindGroupByIdDTO responseDTO = groupService.findGroupById(groupId, userDetails.getUser().getId());
        return ResponseEntity.ok().body(ApiUtils.success(HttpStatus.OK, responseDTO));
    }

    @PatchMapping("/groups/{groupId}")
    public ResponseEntity<?> updateGroup(@RequestBody GroupRequest.UpdateGroupDTO requestDTO, @PathVariable Long groupId, @AuthenticationPrincipal CustomUserDetails userDetails){

        groupService.updateGroup(requestDTO, groupId, userDetails.getUser().getId());
        return ResponseEntity.ok().body(ApiUtils.success(HttpStatus.OK, null));
    }

    @GetMapping("/groups")
    public ResponseEntity<?> findGroupList(@RequestParam("region") String region, @AuthenticationPrincipal CustomUserDetails userDetails){

        GroupResponse.FindAllGroupDTO responseDTO = groupService.findGroupList(userDetails.getUser().getId(), region);
        return ResponseEntity.ok().body(ApiUtils.success(HttpStatus.OK, responseDTO));
    }

    @GetMapping("/groups/local")
    public ResponseEntity<?> findLocalGroup(@RequestParam("region") String region, @RequestParam("page") Integer page, @RequestParam("size") Integer size, @AuthenticationPrincipal CustomUserDetails userDetails){

        GroupResponse.FindLocalGroupDTO responseDTO = groupService.findLocalGroup(userDetails.getUser().getId(), region, page, size);
        return ResponseEntity.ok().body(ApiUtils.success(HttpStatus.OK, responseDTO));
    }

    @GetMapping("/groups/new")
    public ResponseEntity<?> findNewGroup(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @AuthenticationPrincipal CustomUserDetails userDetails){

        GroupResponse.FindNewGroupDTO responseDTO = groupService.findNewGroup(userDetails.getUser().getId(), page, size);
        return ResponseEntity.ok().body(ApiUtils.success(HttpStatus.OK, responseDTO));
    }

    @GetMapping("/groups/my")
    public ResponseEntity<?> findMyGroup(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @AuthenticationPrincipal CustomUserDetails userDetails){

        GroupResponse.FindMyGroupDTO responseDTO = groupService.findMyGroup(userDetails.getUser().getId(), page, size);
        return ResponseEntity.ok().body(ApiUtils.success(HttpStatus.OK, responseDTO));
    }

    @PostMapping("/groups/{groupId}/join")
    public ResponseEntity<?> joinGroup(@RequestBody GroupRequest.JoinGroupDTO requestDTO ,@PathVariable Long groupId, @AuthenticationPrincipal CustomUserDetails userDetails){

        groupService.joinGroup(requestDTO, userDetails.getUser().getId(), groupId);
        return ResponseEntity.ok().body(ApiUtils.success(HttpStatus.OK, null));
    }

    @PostMapping("/groups/{groupID}/join/approve")
    public ResponseEntity<?> approveJoin(@PathVariable Long groupID, @RequestParam("id") Long applicantId, @AuthenticationPrincipal CustomUserDetails userDetails){

        groupService.approveJoin(userDetails.getUser().getId(), applicantId, groupID);
        return ResponseEntity.ok().body(ApiUtils.success(HttpStatus.OK, null));
    }

    @PostMapping("/groups/{groupID}/join/reject")
    public ResponseEntity<?> rejectJoin(@PathVariable Long groupID, @RequestParam("id") Long applicantId, @AuthenticationPrincipal CustomUserDetails userDetails){

        groupService.rejectJoin(userDetails.getUser().getId(), applicantId, groupID);
        return ResponseEntity.ok().body(ApiUtils.success(HttpStatus.OK, null));
    }

    @PostMapping("/groups/{groupId}/like")
    public ResponseEntity<?> likeGroup(@PathVariable Long groupId, @AuthenticationPrincipal CustomUserDetails userDetails){

        groupService.likeGroup(userDetails.getUser().getId(), groupId);
        return ResponseEntity.ok().body(ApiUtils.success(HttpStatus.OK, null));
    }
}