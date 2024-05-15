package web.config;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Main {
    private final HttpHeaders cookies = new HttpHeaders();
    private final RestTemplate restTemplate = new RestTemplate();
    private final String url = "http://94.198.50.185:7081/api/users";

    public List<User> getUsers() {
        ResponseEntity<List<User>> response = restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        String cookietemo = response.getHeaders().get("Set-Cookie").get(0).substring(0, response.getHeaders().get("Set-Cookie")
                .get(0).indexOf(';'));
        if (response.getStatusCode() == HttpStatus.OK) {
            cookies.add("Cookie", cookietemo);
        }
        return response.getBody();
    }

    public ResponseEntity<String> postUsers(User user) {
        cookies.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<>(user, cookies);
        return restTemplate.postForEntity(url, entity, String.class);
    }

    public String putUsers(User user) {
        user.setId(3L);
        user.setName("Tomas");
        user.setLastName("Shelby");
        HttpEntity<User> entity = new HttpEntity<>(user, cookies);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();
    }

    public String deleteUsers(int id) {
        HttpEntity<User> entity = new HttpEntity<>(cookies);
        return restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, entity, String.class).getBody();
    }

    public User changeUserData(long id, String name, String lastname, byte age, User user) {
        user.setId(id);
        user.setName(name);
        user.setLastName(lastname);
        user.setAge(age);
        return user;
    }


    public static void main(String[] args) {
        Main app = new Main();
        for (User u : app.getUsers()) {
            System.out.println(u);
        }
        User user = new User();
        user.setId(3L);
        user.setName("James");
        user.setLastName("Brown");
        user.setAge((byte) 60);

        System.out.println("\n");
        System.out.println(app.postUsers(user).getBody()
                + app.putUsers(user)
                + app.deleteUsers(3));
    }
}