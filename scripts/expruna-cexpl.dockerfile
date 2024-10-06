FROM eneiascs/hylaa:1.0.0

RUN apt update
RUN apt install jq curl bc -y

COPY shell/* /opt/dohko/job/

RUN chmod +x /opt/dohko/job/*