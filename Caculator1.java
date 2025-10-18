import java.util.Stack;

/**
 * 中缀表达式->后缀表达式->运算
 */
public class Caculator1
{
    String exp;
    String postexp="";

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getPostexp() {
        return postexp;
    }

    /**
     * 算术表达式转换成后缀表达式(exp->postexp)
     */
    public void Trans(){
        Stack<Character> opor = new Stack<>();
        StringBuilder num = new StringBuilder();
        boolean isDot = false;
        int i=0;//exp的下标
        char ch,e;
        while(i<exp.length())
        {
            ch=exp.charAt(i);
            if (ch=='(')
                opor.push(ch);
            else if (ch==')')
            {
                if (!num.isEmpty())
                {
                    postexp+=num.toString();
                    num.setLength(0);
                    postexp+="#";
                    isDot = false;
                }
                while(!opor.empty()&&opor.peek()!='(')
                {
                    e=opor.pop();
                    postexp+=e;
                    postexp+="#";
                }
                opor.pop();
            }
            else if (ch=='+'||ch=='-')
            {
                boolean flag=false; //是否为负号
                if (i==0)
                {
                    flag=true;
                }
                else if (exp.charAt(i-1)=='(')
                {
                    flag=true;
                } else if (exp.charAt(i-1)=='+'||exp.charAt(i-1)=='-'||exp.charAt(i-1)=='*'||exp.charAt(i-1)=='/')
                {
                    flag=true;
                }
                if (flag)
                {
                    if (!num.isEmpty())
                    {
                        postexp+=num.toString();
                        num.setLength(0);
//                        postexp+="#";
                        isDot = false;
                    }
                    num.append('-');
                }
                else    //二元运算符
                {
                    if (!num.isEmpty())
                    {
                        postexp+=num.toString();
                        num.setLength(0);
                        postexp+="#";
                        isDot = false;
                    }
                    while(!opor.empty()&&opor.peek()!='(')
                    {
                        e=opor.pop();
                        postexp+=e;
                        postexp+="#";
                    }
                    opor.push(ch);
                }
            }
            else if (ch=='*'||ch=='/')
            {
                if (!num.isEmpty())
                {
                    postexp+=num.toString();
                    num.setLength(0);
                    postexp+="#";
                    isDot = false;
                }
                while (!opor.empty()&&opor.peek()!='('&&(opor.peek()=='*'||opor.peek()=='/'))
                {
                    e=opor.pop();
                    postexp+=e;
                    postexp+="#";
                }
                opor.push(ch);
            }
            else if (Character.isDigit(ch))
            {
                num.append(ch);
            } else if (ch == '.' && !isDot)
            {
                boolean hasDight =false;
                if (!num.isEmpty())
                {
                    for (int k=0;k<num.length();k++)
                    {
                        if (Character.isDigit(num.charAt(k)))
                        {
                            hasDight = true;
                            break;
                        }
                    }
                }
                if (num.isEmpty()||num.toString().contains("-")&& !hasDight)
                {
                    num.append("0");
                }
                num.append('.');
                isDot = true;
            }
            i++;
        }
        if (!num.isEmpty())
        {
            postexp+=num.toString();
            num.setLength(0);
            postexp+="#";
        }
        while(!opor.empty())
        {
            e=opor.pop();
            postexp+=e;
            postexp+="#";
        }
    }

    public double getValue()
    {
        Stack<Double> opand = new Stack<>();
        int i = 0;
        char ch;
        boolean flag;     //是否为一元运算符"-"
        double num=0;
        double a=0,b=0,c=0;

        while (i  < postexp.length())
        {
            flag=false;
            ch=postexp.charAt(i);

            if (ch == '-' && Character.isDigit(postexp.charAt(i+1)))    //一元运算符
            {
                flag=true;
            }


            if (ch == '-' && postexp.charAt(i+1) == '#')
            {
                flag=false;
            }

            if (ch == '-' && postexp.charAt(i+1)=='-'){
                i=i+2;
                ch=postexp.charAt(i);
                int k = 0;
                while (ch == '-')
                {
                    k++;
                    i++;
                    ch=postexp.charAt(i);
                }
                if (k%2==0){
                    flag=false;
                }else {
                    flag=true;
                }
            }

            num=0;
            int k =-1;
            while (Character.isDigit(postexp.charAt(i)) || ch == '.' || (flag && ch == '-'))
            {
                if (ch == '-')
                {
                    i++;
                    ch=postexp.charAt(i);
                }
                num=10*num+(ch-'0');
                i++;
                ch=postexp.charAt(i);
                if (ch == '.')
                {
                    i++;
                    ch=postexp.charAt(i);
                    num=num + Math.pow(10,k)*Character.getNumericValue(ch);
                    k--;
                    i++;
                    ch=postexp.charAt(i);
                }

                if (ch == '#')
                {
                    if (flag)
                    {
                        num=-num;
                    }
                    opand.push(num);
                }
            }
            switch (ch)
            {
                case '+':
                    a = opand.pop();
                    b = opand.pop();
                    c = a + b;
                    opand.push(c);
                    break;
                case '-':
                    if (!flag)
                    {
                        a=opand.pop();
                        b=opand.pop();
                        c=b-a;
                        opand.push(c);
                    }
                    break;
                case '*':
                    a=opand.pop();
                    b=opand.pop();
                    c=b*a;
                    opand.push(c);
                    break;
                case '/':
                    a=opand.pop();
                    b=opand.pop();
                    if (a!=0)
                    {
                        c=b/a;
                        opand.push(c);
                    }
                    else
                        throw new ArithmeticException("运算错误：除零");
                    break;
            }
            i++;
        }
        if (opand.size()!=1)
        {
            throw new ArithmeticException("算数异常");
        }
        return opand.peek();
    }

    public static void main(String[] args) {
        Caculator1 caculator1 = new Caculator1();

//        String str1 = "(56-20)/(4-2)";
//        String str1 = "(2.5-0.5)*2";
//        String str1 = "(10 + 20) * 3 - 15 / 5";
//        String str1 = "3.5 + (6.2 - 2.7) * 2";
//        String str1 = "100 / (25 - 20) + 18";
//        String str1 = "((15 - 5) * 2 + 8) / (3 + 1)";
//        String str1 = "10 - (-4)";
//        String str1 = "10 - (3 + (-4) * 2)";
//        String str1 = "(-2.5 + 7.5) * 2";
//        String str1 = "20 / (5 - (-3)) + (-2)";
//        String str1 = "((-10) + 25) / 3";
//        String str1 = "-0.5 * 4 + 6 / (-3)";
//        String str1 = "3.5 + 2.5";
//        String str1 = "(15 - (-5) * 2 + 8) / (4 - (-2))";
//        String str1 = "-0.5 * (3 + (-4.5 / 1.5)) - 2";
//        String str1 = "((-10 + 20) * 0.5 - 3) / (-2) + 7";
//        String str1 = "10 - (2.5 + (-3) * 4 / (-2))";
//        String str1 = "(-.5 + 2.5) * (3 - (-6 / 3)) - 10";
        String str1 = "----(-8)";


        caculator1.setExp(str1);
        caculator1.Trans();

        String postexp = caculator1.getPostexp();
        Double value = caculator1.getValue();

        System.out.printf("%s的后缀表达式：%s",str1,postexp);
        System.out.println();
        System.out.printf("%s=%s",str1,value);

    }
}


