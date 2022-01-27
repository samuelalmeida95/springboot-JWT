package autenticacao.jwt.exceptions;

public class SenhaInvalidaException extends RuntimeException {
   public SenhaInvalidaException(){
       super("Senha Invalida..");
   }
}
