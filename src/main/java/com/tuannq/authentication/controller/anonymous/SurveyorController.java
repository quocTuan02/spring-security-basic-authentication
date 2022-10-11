package com.tuannq.authentication.controller.anonymous;


import com.tuannq.authentication.entity.survey.*;
import com.tuannq.authentication.exception.ArgumentException;
import com.tuannq.authentication.model.request.SurveyAddForm;
import com.tuannq.authentication.model.request.SurveySearchForm;
import com.tuannq.authentication.model.response.PageResponse;
import com.tuannq.authentication.model.response.SuccessResponse;
import com.tuannq.authentication.repository.survey.QuestionnaireRepository;
import com.tuannq.authentication.util.AuthUtils;
import com.tuannq.authentication.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.tuannq.authentication.config.DefaultVariable.LIMIT;
import static com.tuannq.authentication.util.ConverterUtils.isEmpty;
import static com.tuannq.authentication.util.ConverterUtils.isNotEmpty;

/**
 * This class controls pages that used to create questionnaires
 */
@Controller
@RequiredArgsConstructor
public class SurveyorController {
    private final QuestionnaireRepository questionnaireRepository;
    private final AuthUtils authUtils;

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public String questionnaireCreatesForm() {
        return "survey/qCreate";
    }

    @GetMapping("survey/create")
    public String questionnaireCreateForm() {
        return "survey/create";
    }

    @PostMapping("api/survey")
    public ResponseEntity<?> addSurvey(@RequestBody @Validated SurveyAddForm form) {
        if (isEmpty(form.getEssayQuestions()) && isEmpty(form.getMultipleChoiceQuestions())) {
            throw new ArgumentException("essayQuestions", "not-null");
        }
        var questionnaire = new Questionnaire();
        questionnaire.setName(form.getSurveyName());
        if (isNotEmpty(form.getEssayQuestions())){
            for (var question : form.getEssayQuestions()) {
                var openEnd = new OpenEnd();
                openEnd.setQuestion(question);
                questionnaire.addQuestion(openEnd);
            }
        }
        if (isNotEmpty(form.getMultipleChoiceQuestions())){
            for (var question : form.getMultipleChoiceQuestions()) {
                var selection = new Selection(question.getName(), question.getAnswers());
                questionnaire.addQuestion(selection);
            }
        }

        questionnaireRepository.save(questionnaire);
        return ResponseEntity.ok(new SuccessResponse<>());
    }


    @GetMapping("/view/{id}")
    public String viewQuestionnaire(@PathVariable long id, Model model) {
        var userSurveys = new ArrayList<Questionnaire>();
        Questionnaire questionnaire = this.questionnaireRepository.findById(id);
        if (userSurveys.contains(questionnaire)) {
            model.addAttribute("error", "Sorry, you cannot do your own survey ID : " + id + ". Try other surveys later.");
            return "survey/errorRedirect";
        }
        if (questionnaire == null) {
            model.addAttribute("error", "The survey " + id + " does not exist, try another id later.");
            return "survey/errorRedirect";
        }
        if (questionnaire.isClosed()) {
            model.addAttribute("error", "The survey is closed, try another id later.");
            return "survey/errorRedirect";
        }
        model.addAttribute("questionnaire", questionnaire);
        List<Long> openEndIdList = new ArrayList<>();
        List<Long> rangeIdList = new ArrayList<>();
        List<Long> selectionIdList = new ArrayList<>();
        for (var question : questionnaire.getQuestionList()) {
            if (question instanceof OpenEnd) {
                openEndIdList.add(question.getId());
            } else if (question instanceof Range) {
                rangeIdList.add(question.getId());
            } else if (question instanceof Selection) {
                selectionIdList.add(question.getId());
            }
        }
        model.addAttribute("openEndList", openEndIdList);
        model.addAttribute("rangeList", rangeIdList);
        model.addAttribute("selectionList", selectionIdList);
        return "survey/doSurvey";
    }

    @PostMapping("/view/{id}")
    public String submitSurveyAnswer(
            @PathVariable long id,
            String[] answer,
            HttpServletResponse resp
    ) throws IOException {
        Questionnaire questionnaire = this.questionnaireRepository.findById(id);
        PrintWriter out = resp.getWriter();
        if (questionnaire == null) {
            return null;
        }
        for (int i = 0; i < answer.length; i++) {
            if (answer[i].equals("")) {
                out.println("<script language='javascript'>alert('You still have incomplete question(s). Fill them all and submit again.')</script>");
                out.println("<script language='javascript'>window.location.href='/view/" + id + "'</script>");
            }
        }
        int i = 0;
        for (var question : questionnaire.getQuestionList()) {
            question.addAnswer(answer[i]);
            i++;
        }
        questionnaireRepository.save(questionnaire);
        return "survey/completeSurvey";
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public String questionnaireCreate(
            HttpServletRequest request,
            HttpServletResponse resp,
            Authentication authentication
    ) throws IOException {
        Questionnaire questionnaire = new Questionnaire();
        Map<String, String[]> formData = request.getParameterMap();
        PrintWriter out = resp.getWriter();
        for (Map.Entry<String, String[]> entry : formData.entrySet()) {
            if (entry.getKey().equals("survey_name")) {
                for (String name : entry.getValue()) {
                    if (name.equals("")) {
                        out.println("<script language='javascript'>alert('Please enter a survey name! Fill them all and submit again.')</script>");
                        out.println("<script language='javascript'>window.location.href='/create'</script>");
                    } else {
                        questionnaire.setName(name);
                    }
                }
            }
            if (entry.getKey().equals("open_end_question")) {
                for (String question : entry.getValue()) {
                    if (question.equals("")) {
                        out.println("<script language='javascript'>alert('You still have incomplete question(s). Fill them all and submit again.')</script>");
                        out.println("<script language='javascript'>window.location.href='/create'</script>");
                    } else {
                        OpenEnd q = new OpenEnd();
                        q.setQuestion(question);
                        questionnaire.addQuestion(q);
                    }
                }
            } else if (entry.getKey().equals("range_question")) {
                String[] question_content = entry.getValue().clone();
                for (int i = 0; i < question_content.length; i = i + 3) {
                    if (question_content[i].equals("") || question_content[i + 1].equals("") || question_content[i + 2].equals("")) {
                        out.println("<script language='javascript'>alert('You still have incomplete question(s). Fill them all and submit again.')</script>");
                        out.println("<script language='javascript'>window.location.href='/create'</script>");
                    } else if ((!((question_content[i + 1].matches("^[0-9]+$")) && (question_content[i + 2].matches("^[0-9]+$"))))) {
                        out.println("<script language='javascript'>alert('Max boundary or Min boundary should be integer. Fill them all and submit again.')</script>");
                        out.println("<script language='javascript'>window.location.href='/create'</script>");

                    } else if (Integer.parseInt(question_content[i + 1]) >= Integer.parseInt(question_content[i + 2])) {
                        out.println("<script language='javascript'>alert('Max boundary should larger than Min boundary. Fill them all and submit again.')</script>");
                        out.println("<script language='javascript'>window.location.href='/create'</script>");
                    } else {
                        Range q = new Range(question_content[i], Integer.parseInt(question_content[i + 1]), Integer.parseInt(question_content[i + 2]));
                        questionnaire.addQuestion(q);
                    }
                }
            } else if (entry.getKey().equals("selection_question")) {
                String[] question_content = entry.getValue().clone();
                for (int i = 0; i < question_content.length; i = i + 2) {
                    if (question_content[i].equals("") || question_content[i + 1].equals("")) {
                        out.println("<script language='javascript'>alert('You still have incomplete question(s). Fill them all and submit again.')</script>");
                        out.println("<script language='javascript'>window.location.href='/create'</script>");
                    } else {
                        String[] list = question_content[i + 1].split(",");
                        List<String> l = Arrays.asList(list);
                        Selection q = new Selection(question_content[i], l);
                        questionnaire.addQuestion(q);
                    }
                }

            }
        }
        if (questionnaire.getQuestionList().size() <= 0) {
            out.println("<script language='javascript'>alert('Please add a question! Fill them all and submit again.')</script>");
            out.println("<script language='javascript'>window.location.href='/create'</script>");
        } else {
            /*TODO save questionnaire*/
            questionnaireRepository.save(questionnaire);
//            User user = this.userRepo.findByUsername(authentication.getName());
//            user.addQuestion(questionnaire);
//            this.userRepo.save(user);
            //long id = questionnaire.getId();
            //return "survey/redirect:/view/" + id;
        }
        return "survey/createSurvey";
    }

    @RequestMapping(path = "/result/{id}", method = RequestMethod.GET)
    public String displayQuestion(@PathVariable long id, Model model) {
        Questionnaire questionnaire = this.questionnaireRepository.findById(id);
        List<Question> questions = questionnaire.getQuestionList();
        List<OpenEnd> openEndsQuestion = new ArrayList<>();
        List<Range> rangeQuestion = new ArrayList<>();
        List<Selection> selectionQuestion = new ArrayList<>();

        boolean isEmpty = false;
        if (questionnaire.getQuestionList().isEmpty()) {
            isEmpty = true;
        } else {
            model.addAttribute("Questionnaire", questionnaire);
            for (Question question : questions) {
                if (question instanceof OpenEnd) {
                    openEndsQuestion.add((OpenEnd) question);
                } else if (question instanceof Range) {
                    rangeQuestion.add((Range) question);
                } else if (question instanceof Selection) {
                    selectionQuestion.add((Selection) question);
                }
            }
            model.addAttribute("openEndQuestionList", openEndsQuestion);
            model.addAttribute("rangeQuestionList", rangeQuestion);
            model.addAttribute("selectionQuestionList", selectionQuestion);
        }
        model.addAttribute("isEmpty", isEmpty);
        return "survey/Question";
    }

    @GetMapping("survey")
    public String surveys(
            Model model,
            SurveySearchForm form
    ) {
        var identity = authUtils.getUser().get();
        var data = this.questionnaireRepository.findByCreatedBy(
                identity,
                form,
                new PageUtil(form.getPage(), LIMIT, form.getOrder(), form.getDirection()).getPageRequest()
        );
        var surveys = new PageResponse<>(data);
        model.addAttribute("surveys", surveys);
        model.addAttribute("totalPages", surveys.getTotalPages());
        model.addAttribute("currentPage", surveys.getCurrentPage());
        return "survey/list";
    }

    @GetMapping("allSurvey")
    public String showAllSurvey(Model model) {

        List<Questionnaire> allSurveys = this.questionnaireRepository.findAllByOrderByIdAsc();

        allSurveys.removeIf(Questionnaire::isClosed);

        model.addAttribute("surveys", allSurveys);
        return "survey/allSurvey";
    }
}
