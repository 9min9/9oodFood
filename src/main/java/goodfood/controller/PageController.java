package goodfood.controller;

import goodfood.controller.dto.user.login.LoginRequest;
import goodfood.controller.dto.user.login.TokenInfo;
import goodfood.controller.dto.user.passwordreset.PasswordResetRequest;
import goodfood.controller.dto.user.signup.SignUpRequest;
import goodfood.exception.user.NotFoundUserException;
import goodfood.exception.user.UnMatchedPasswordException;
import goodfood.service.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final MemberService memberService;

    /** public page */
    @GetMapping("/")
    public String index() {
        return "public/index";
    }

    @GetMapping("login")
    public String loginForm(@ModelAttribute LoginRequest request, HttpServletRequest httpRequest) {
        String referer = httpRequest.getHeader("Referer");

        if(referer != null && !referer.contains("/login")) {
            httpRequest.getSession().setAttribute("prevPage", referer);
        }


        return "public/account/login";
    }
    @PostMapping("login")
    public String loginProcess(@ModelAttribute @Valid LoginRequest request, BindingResult bindingResult, HttpServletRequest httpRequest, Model model) {
        try {
            System.out.println("http servlet!" + httpRequest.getHeader("errorMessage"));

            TokenInfo login = memberService.login(request.toServiceDto());
            if(!request.getUsername().isEmpty() && !request.getPassword().isEmpty()) {

                if (login.getUserRole().equals("ROLE_ADMIN")) {
                    return "redirect:/admin";
                }
            }

        } catch (NotFoundUserException | UnMatchedPasswordException e) {
            bindingResult.reject("user.login.error", "아이디 또는 비밀번호가 일치하지 않습니다.");
        } finally {
            if (bindingResult.hasErrors()) {
                return "public/account/login";
            }
            return "redirect:/";
        }
    }

    @GetMapping("/signup")
    public String signup(@ModelAttribute SignUpRequest request) {
        return "public/account/signUp";
    }

    @GetMapping("/password/reset/validate")
    public String passwordResetPage(@ModelAttribute PasswordResetRequest request) {
        return "public/account/passwordResetValidate";
    }

    @GetMapping("/store/{category}/{location}")
    public String listedStorePage(@PathVariable("category")String category, @PathVariable("location")String location) {
        if(category.equals("restaurant")) {
            if(location.equals("seoul")) {
                return "public/store/restaurant/seoulRest";
            } else if (location.equals("gyeonggi")) {
                return "public/store/restaurant/gyeonggiRest";
            } else if (location.equals("etc")) {
                return "public/store/restaurant/etcRest";
            } else {
                throw new RuntimeException("URL Location Error");
            }
        } else if (category.equals("cafe")) {
            if(location.equals("seoul")) {
                return "public/store/cafe/seoulCafe";
            } else if (location.equals("gyeonggi")) {
                return "public/store/cafe/gyeonggiCafe";
            } else if (location.equals("etc")) {
                return "public/store/cafe/etcCafe";
            } else {
                throw new RuntimeException("URL Location Error");
            }
        } else if (category.equals("bar")) {
            if(location.equals("seoul")) {
                return "public/store/bar/seoulBar";
            } else if (location.equals("gyeonggi")) {
                return "public/store/bar/gyeonggiBar";
            } else if (location.equals("etc")) {
                return "public/store/bar/etcBar";
            } else {
                throw new RuntimeException("URL Location Error");
            }
        } else {
            throw new RuntimeException("URL Category Error");
        }
    }



    @GetMapping("/forum/detail")
    public String detailedStorePage() {
        return "public/forum/showDetailedForum";
    }

    /** user page */

    @GetMapping("/user/liked/{category}")
    public String userLikedPage(@PathVariable("category") String category){
        if (category.equals("restaurant")) {
            return "user/store/likedRestaurant";
        } else if (category.equals("cafe")) {
            return "user/store/likedCafe";
        } else {
            return "user/store/likedBar";
        }
    }

    @GetMapping("/user/mypage")
    public String userMyPage() {
        return "user/mypage";
    }

    @GetMapping("/forum/update")
    public String updateStorePage() {
        return "user/forum/updateForum";
    }

    @GetMapping("/store/insert")
    public String insertStorePage() {
        return "user/forum/createForum";
    }

    /** admin page */

    @GetMapping("/admin")
    public String adminIndex() {
        return "admin/index";
    }

    @GetMapping("/admin/store/config")
    public String storeConfig() {
        return "admin/storeConfig";
    }

    @GetMapping("/admin/user/config")
    public String userConfig() {
        return "admin/userConfig";
    }




}
