file_env() {
    local var="$1"
    local fileVar="${var}_FILE"
    local def="${2:-}"
    if [[ ${!var:-} && ${!fileVar:-} ]]; then
        echo >&2 "error: both $var and $fileVar are set (but are exclusive)"
        exit 1
    fi
    local val="$def"
    if [[ ${!var:-} ]]; then
        val="${!var}"
    elif [[ ${!fileVar:-} ]]; then
        val="$(< "${!fileVar}")"
    fi

    if [[ -n $val ]]; then
        export "$var"="$val"
    fi

    unset "$fileVar"
}
file_env 'SPRING_DATASOURCE_URL'
file_env 'SPRING_DATASOURCE_USERNAME'
file_env 'SPRING_DATASOURCE_PASSWORD'

exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Dlogback.logging.config=/app/resources/logback-spring.xml  -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "tech.dut.fasto.FastoApplication"  "$@"
